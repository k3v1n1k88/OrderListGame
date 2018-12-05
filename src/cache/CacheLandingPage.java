/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import configuration.ConfigCache;
import configuration.ConfigConnectionPool;
import configuration.ConfigSystem;
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
import exception.DatabaseException;
import java.util.ArrayList;
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

    // This use for cache Recommendation game
    private static final int DEFAULT_EXPIRE_TIME = 20;
    private static final int DEFAULT_REFRESH_TIME = 15;

    // This using like keys. So, we can get list from this cache
    private static final String VISUAL_KEY = "list";

    private static DatabaseConnectionPoolRedis pool;
    private static ConfigSystem configSystem;
    private static CacheLandingPage instance;

    private LoadingCache<String, ListGame> cacheLandingPage;
    private LoadingCache<String, List<String>> cacheRecommendationGameList;

    private ConfigCache configCache;

    /**
     * Because, database recommendation cannot overload when get list from
     * session continuously So, we create this cache. It will auto-update after
     * default duration configured at {@link CacheListRecommendationGame#DEFAULT_REFRESH_TIME}
     * {@link CacheListRecommendationGame#DEFAULT_EXPIRE_TIME}
     */
    static {
        try {
            ConfigConnectionPool config = new ConfigConnectionPool();
            DatabaseRedisConnectionFactory factory = new DatabaseRedisConnectionFactory();
            pool = new DatabaseConnectionPoolRedis(factory, config);
            configSystem = new ConfigSystem();
        } catch (ConfigException ex) {
//            System.out.println(ex.getMessage());
            logger.info(ex.getMessage());
//            System.exit(0);
        }
    }

    public static CacheLandingPage getInstance() throws ConfigException {
        if (instance == null) {
            synchronized (CacheLandingPage.class) {
                instance = new CacheLandingPage();
            }
        }
        return instance;
    }

    private CacheLandingPage(ConfigCache config) {

        this.configCache = config;

        // Initialize cache Recommendation Game
        CacheLoader loaderCache = new CacheLoader<String, List<String>>() {
            @Override
            public List<String> load(String k) throws PoolException {
                DatabaseRedisConnection dbcnn = null;
                return loadDataRecommendationGameHelper();
            }

            @Override
            public ListenableFuture<List<String>> reload(String key, List<String> oldValue) throws Exception {
                ExecutorService executor = Executors.newFixedThreadPool(1);
                ListenableFutureTask<List<String>> taskGetList = ListenableFutureTask.create(new Callable<List<String>>() {
                    @Override
                    public List<String> call() throws PoolException, DatabaseException {
                        List<String> listGame = loadDataRecommendationGameHelper();
                        if (listGame != null) {
                            return listGame;
                        } else {
                            throw new DatabaseException("Cannot load list recommendation game");
                        }
                    }
                });
                executor.execute(taskGetList);
                return taskGetList;
            }
        };

        this.cacheRecommendationGameList = CacheBuilder.newBuilder()
                .expireAfterAccess(DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES)
                .refreshAfterWrite(DEFAULT_REFRESH_TIME, TimeUnit.MINUTES)
                .build(loaderCache);

        // Initialize cache landing page
        // Create cache loader
        CacheLoader<String, ListGame> loaderCacheLandingPage = new CacheLoader<String, ListGame>() {
            @Override
            public ListGame load(String session) throws PoolException, DatabaseException {
                ListGame listGame = loadListGameHelper(session);
                if (listGame != null) {
                    return listGame;
                } else {
                    throw new DatabaseException("Cannot find session \""+session+"\" in database");
                }

            }

            @Override
            public ListenableFuture<ListGame> reload(String session, ListGame oldList) {
                ExecutorService executor = Executors.newFixedThreadPool(1);
                ListenableFutureTask<ListGame> taskGetList = ListenableFutureTask.create(new Callable<ListGame>() {
                    @Override
                    public ListGame call() throws PoolException, DatabaseException {
                        ListGame listGame = loadListGameHelper(session);
                        if (listGame != null) {
                            return listGame;
                        } else {
                            throw new DatabaseException("Cannot find session \""+session+"\" in database");
                        }
                    }
                });
                executor.execute(taskGetList);
                return taskGetList;
            }
        };

        cacheLandingPage = CacheBuilder.newBuilder()
                .maximumSize(configCache.getMaximumSize())
                .expireAfterWrite(configCache.getExpireAfterWrite(), TimeUnit.SECONDS)
                .refreshAfterWrite(configCache.getRefreshAfterWrite(), TimeUnit.SECONDS)
                .build(loaderCacheLandingPage);

    }

    private CacheLandingPage() throws ConfigException {
        this(new ConfigCache());
    }

    public ListGame getList(String session) throws ExecutionException {
        try {
            return this.cacheLandingPage.get(session);
        } catch (ExecutionException ex) {
            logger.error(ex);
            throw ex;
        }
    }

    public List<String> getListRecommendation() throws ExecutionException {
        try {
            return this.cacheRecommendationGameList.get(VISUAL_KEY);
        } catch (ExecutionException ex) {
            logger.error("Cannot get list recommendation from cache", ex);
            throw ex;
        }
    }

    private ListGame loadListGameHelper(String session) {

        DatabaseRedisConnection dbcnn = null;
        ListGame retListGame = null;

        List<String> listGameIDScoring = null;
        List<String> listGameIDRecommendation = null;

        try {

            dbcnn = pool.borrowObjectFromPool();

            Jedis jedis = dbcnn.getConnection();
//            jedis.select(0);

            Map<String, String> listGame = jedis.hgetAll(session);

            if (listGame.size() <= 0) {
                return null;
            }

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

            // Get list Recommendation game from cache.
            listGameIDRecommendation = new ArrayList<>();
            listGameIDRecommendation.addAll(cacheRecommendationGameList.get(VISUAL_KEY));

            // Remove all games in list scoring from list recommendation 
            for (String s : listGameIDScoring) {
                listGameIDRecommendation.remove(s);
            }

            // Init list game
            retListGame = new CacheLandingPage.ListGame();
            retListGame.setScoringList(listGameIDScoring);
            retListGame.setRecommandatioList(listGameIDRecommendation);

            return retListGame;
        } catch (PoolException ex) {
            logger.error("Error when borrow connection to load list game from database", ex);
        } catch (ExecutionException ex) {
            logger.error("Error when get info from cache recommendation game", ex);
        } finally {
            if (dbcnn != null) {
                try {
                    pool.returnObjectToPool(dbcnn);
                } catch (PoolException ex) {
                    logger.error("Error when return connection after load list game from database", ex);
                }
            }
        }
        return null;
    }

    private List<String> loadDataRecommendationGameHelper() {
        DatabaseRedisConnection dbcnn = null;
        try {
            dbcnn = pool.borrowObjectFromPool();
            Jedis jedis = dbcnn.getConnection();
            jedis.select(constant.DBConstantString.DATABASE_RECOMMENDATION_INDEX);
            List<String> listGame = jedis.sort(constant.DBConstantString.LIST_GAME, new SortingParams()
                    .by(constant.DBConstantString.GAME_ID + ":*->".concat(constant.DBConstantString.POINT))
            );
            logger.info(listGame.size());
            return listGame;
        } catch (PoolException ex) {
            logger.error("Error when borrow connection from pool to load data", ex);
        } finally {
            if (dbcnn != null) {
                try {
                    pool.returnObjectToPool(dbcnn);
                } catch (PoolException ex) {
                    logger.error("Error when return connection after load data to cache from database", ex);
                }
            }
        }
        return null;
    }

    // This class created to help for cache landingpage can holder two list scroing and recommendation game
    public class ListGame {

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

}
