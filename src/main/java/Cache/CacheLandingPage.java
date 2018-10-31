/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cache;

import Configuration.ConfigCache;
import Configuration.ConfigConnectionPool;
import Configuration.ConfigOfSystem;
import DatabaseConnectionPool.DatabaseConnectionPoolRedis;
import DatabaseConnectionPool.DatabaseRedisConnection;
import DatabaseConnectionPool.DatabaseRedisConnectionFactory;
import Exception.ConfigException;
import Exception.PoolException;
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
    
    static{
        try {
            ConfigConnectionPool config = new ConfigConnectionPool();
            DatabaseRedisConnectionFactory factory = new DatabaseRedisConnectionFactory();
            pool = new DatabaseConnectionPoolRedis(factory,config);
            configSystem = new ConfigOfSystem();
        } catch (ConfigException ex) {
            System.out.println(ex.getMessage());
            logger.info(ex.getMessage());
            System.exit(0);
        }
    }
    
    private LoadingCache<String,ListGame> cache;
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
        cache = CacheBuilder.newBuilder()
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
            return this.cache.get(session);
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

            // Get list Recommendation game
            jedis.select(Constant.DBConstantString.DATABASE_RECOMMENDATION_INDEX);
            listGameIDRecommendation = jedis.sort(Constant.DBConstantString.LIST_GAME, new SortingParams().
                    by(Constant.DBConstantString.GAME_ID + ":*->".concat(Constant.DBConstantString.POINT))
            );

            // Init list game
            retListGame = new CacheLandingPage.ListGame();
            retListGame.setScoringList(listGameIDScoring);
            retListGame.setScoringList(listGameIDScoring);

            return retListGame;
        } catch (PoolException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } finally {
            if (dbcnn != null) {
                pool.returnObjectToPool(dbcnn);
            }
        }
    }
    
    //    public static class CacheListRecommendationGame{
//        private static List<String> listGameID = null;
//        
//        public static List<String> getList() throws PoolException{
//            if(listGameID == null){
//                synchronized(CacheListRecommendationGame.class){
//                    try {
//                        DatabaseRedisConnection dbcnn = pool.borrowObjectFromPool();
//                        Jedis jedis = dbcnn.getConnection();
//                        jedis.select(Constant.DBConstantString.DATABASE_RECOMMENDATION_INDEX);
//                        
//                        Set<String> redisKeys = jedis.keys("samplekey*");
//                        Iterator<String> it = redisKeys.iterator();
//                        TreeMap<String,String> treeMap = new TreeMap<>();
//                        Comparator<Map.Entry<String,String>> comp = new Comparator<>();
//                        while(it.hasNext()){
//                            Trr
//                        }
//                    } catch (PoolException ex) {
//                        logger.error(ex.getMessage());
//                        throw ex;
//                    }
//                }
//            }
//            return listGameID;
//        }
//    }
    
}
