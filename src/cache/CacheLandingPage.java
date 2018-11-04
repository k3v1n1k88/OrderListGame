/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import configuration.ConfigCache;
import configuration.ConfigConnectionPool;
import configuration.ConfigOfSystem;
import database.connection.DatabaseConnectionPoolRedis;
import database.connection.DatabaseRedisConnection;
import database.connection.DatabaseRedisConnectionFactory;
import exception.ConfigException;
import exception.PoolException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;
import object.value.database.ScoreValueWrapper;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;

/**
 *
 * @author root
 */
public class CacheLandingPage {
    
    private static final Logger logger = Logger.getLogger(CacheLandingPage.class);
    
    private static DatabaseConnectionPoolRedis pool;
    private static ConfigOfSystem configSystem;
    
    /** 
    * Because, database recommendation cannot overload when get list from session continuously
    * So, we create this cache. 
    * It will auto-update after default duration 
    * configured at {@link CacheListRecommendationGame#DEFAULT_REFRESH_TIME}
    * {@link CacheListRecommendationGame#DEFAULT_EXPIRE_TIME}
    */
    private static CacheListRecommendationGame cacheRecommendationGameList;
    
    static{
        try {
            ConfigConnectionPool config = new ConfigConnectionPool();
            DatabaseRedisConnectionFactory factory = new DatabaseRedisConnectionFactory();
            pool = new DatabaseConnectionPoolRedis(factory,config);
            configSystem = new ConfigOfSystem();
            cacheRecommendationGameList = new CacheListRecommendationGame(configSystem.getLimitRecommendationGame());
        } catch (ConfigException ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage());
            System.exit(0);
        }
    }
    
    private LoadingCache<String,ListGame> cacheLandingPage;
    private ConfigCache configCache;
    
    public CacheLandingPage(ConfigCache config){
        
        this.configCache = config;
        // Create cache loader
        CacheLoader<String,ListGame> loader = new CacheLoader<String,ListGame>(){
            @Override
            public ListGame load(String session) throws PoolException{
                return CacheLandingPage.load(session);
            }

            @Override
            public ListenableFuture<ListGame> reload(String session, ListGame oldList){
                ExecutorService executor = Executors.newFixedThreadPool(1);
                ListenableFutureTask<ListGame> taskGetList = ListenableFutureTask.create(new Callable<ListGame>() {
                   @Override
                   public ListGame call() throws PoolException {
                       return CacheLandingPage.load(session);
                   }
                 });
                 executor.execute(taskGetList);
                 return taskGetList;
            }  
        };
        
        // Initalize cache
        cacheLandingPage = CacheBuilder.newBuilder()
                .maximumSize(configCache.getMaximumSize())
                .expireAfterWrite(configCache.getExpireAfterWrite(), TimeUnit.SECONDS)
                .refreshAfterWrite(configCache.getRefreshAfterWrite(), TimeUnit.SECONDS)
                .build(loader);
        
    }
    
    public CacheLandingPage() throws ConfigException{
        this(new ConfigCache());
    }
    
    public ListGame getList(String session) throws ExecutionException{
        try{
            return this.cacheLandingPage.get(session);
        } catch (ExecutionException ex) {
            logger.error(ex);
            throw ex;
        }    
    }
    
    public static class ListGame{
        
        private List<String> scoringList;
        private List<String> recommandationList;

        public List<String> getScoringList() {
            return scoringList;
        }

        public void setScoringList(List<String> scoringList) {
            this.scoringList = scoringList;
        }

        public List<String> getRecommandatioList() {
            return recommandationList;
        }

        public void setRecommandatioList(List<String> recommandatioList) {
            this.recommandationList = recommandatioList;
        }
        
    }
    
    private static ListGame load(String session) throws PoolException{
        DatabaseRedisConnection dbcnn = null;
        ListGame retListGame = null;
        List<String> listGameIDScoring = null;
        List<String> listGameIDRecommendation = null;
        try {
            dbcnn = pool.borrowObjectFromPool();
            Jedis jedis = dbcnn.getConnection();

            Map<String, String> listGame = jedis.hgetAll(session);

            Comparator<String> comparator = new Comparator<String>() {
                @Override
                public int compare(String gameID1, String gameID2) {
                    String info1 = listGame.get(gameID1);
                    String info2 = listGame.get(gameID2);

                    ScoreValueWrapper scoreVal1 = ScoreValueWrapper.parse(info1);
                    ScoreValueWrapper scoreVal2 = ScoreValueWrapper.parse(info2);

                    return Long.compare(scoreVal1.getScore(), scoreVal2.getScore());
                }
            };

            // Get list Scoring game
            listGameIDScoring = listGame.keySet().
                    stream().
                    sorted(comparator).
                    collect(Collectors.toList());

            // Get list Recommendation game from cache
            listGameIDRecommendation = CacheLandingPage.cacheRecommendationGameList.get();

            // Init list game
            retListGame = new CacheLandingPage.ListGame();
            retListGame.setScoringList(listGameIDScoring);
            retListGame.setRecommandatioList(listGameIDRecommendation);

            return retListGame;
        } catch (PoolException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (ExecutionException ex) {
            logger.error(ex.getMessage());
        } finally {
            if (dbcnn != null) {
                pool.returnObjectToPool(dbcnn);
            }
        }
        return null;
    }
    
    /**
     * This class support for getting list from database recommendation game
     */
    public static class CacheListRecommendationGame{
        
        private int maxNumsGame;
        
        private LoadingCache<String,List<String>> cache;
        
        public CacheListRecommendationGame(int max){
            this.maxNumsGame = max;
            
            // Create cache loader
            CacheLoader loaderCache = new CacheLoader<String,List<String>>() {
                
                @Override
                public List<String> load(String k) throws PoolException{
                    DatabaseRedisConnection dbcnn = null;
                    return loadDataRecommendationGame();
                }

                @Override
                public ListenableFuture<List<String>> reload(String key, List<String> oldValue) throws Exception {
                    ExecutorService executor = Executors.newFixedThreadPool(1);
                    ListenableFutureTask<List<String>> taskGetList = ListenableFutureTask.create(new Callable<List<String>>() {
                       @Override
                       public List<String> call() throws PoolException {
                           return loadDataRecommendationGame();
                       }
                     });
                     executor.execute(taskGetList);
                     return taskGetList;
                }
            };
            
            // initalize cache
            this.cache = CacheBuilder.newBuilder()
                        .expireAfterAccess(DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES)
                        .refreshAfterWrite(DEFAULT_REFRESH_TIME, TimeUnit.MINUTES)
                        .build(loaderCache);
            
        }
        
        private List<String> loadDataRecommendationGame() throws PoolException {
            DatabaseRedisConnection dbcnn = null;
            try {
                dbcnn = pool.borrowObjectFromPool();
                Jedis jedis = dbcnn.getConnection();
                jedis.select(constant.DBConstantString.DATABASE_RECOMMENDATION_INDEX);
                List<String> listGame = jedis.sort(constant.DBConstantString.LIST_GAME, new SortingParams()
                        .by(constant.DBConstantString.GAME_ID + ":*->".concat(constant.DBConstantString.POINT))
                        .limit(0, this.maxNumsGame)
                );
                return listGame;
            } catch (PoolException ex) {
                logger.error(ex);
                throw ex;
            } finally {
                if (dbcnn != null) {
                    pool.returnObjectToPool(dbcnn);
                }
            }
        }
        
        private static final int DEFAULT_EXPIRE_TIME = 20;
        private static final int DEFAULT_REFRESH_TIME = 15;
        
        // This using like keys. So, we can get list from this cache
        private static final String KEY = "list";

        private List<String> get() throws ExecutionException {
            return this.cache.get(KEY);
        }
    }
    
}
