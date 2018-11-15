/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;

import strategy.calculation.PointCalculation;
import strategy.calculation.ReCalculation;
import configuration.ConfigConnectionPool;
import configuration.ConfigOfSystem;
import database.connection.DatabaseConnectionPoolRedis;
import database.connection.DatabaseRedisConnection;
import database.connection.DatabaseRedisConnectionFactory;
import exception.CalculationException;
import exception.ConfigException;
import exception.PoolException;
import object.log.LogLandingPage;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import object.value.database.ScoreValueWrapper;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 *
 * @author root
 */
public class WorkerLogLandingPage extends WorkerAbstract<LogLandingPage>{

    private static final Logger logger = Logger.getLogger(WorkerLogLandingPage.class);
    
    private static ConfigOfSystem configSystem;
    
    private static DatabaseConnectionPoolRedis pool;
    
    static{
        try {
            ConfigConnectionPool config = new ConfigConnectionPool();
            DatabaseRedisConnectionFactory factory = new DatabaseRedisConnectionFactory();
            pool = new DatabaseConnectionPoolRedis(factory,config);
            configSystem = new ConfigOfSystem();
        } catch (ConfigException ex) {
            logger.info(ex.getMessage());
            System.exit(0);
        }
    }
    
    public WorkerLogLandingPage(LogLandingPage log){
        super(log);
    }
    
    @Override
    protected boolean processLog() {
        DatabaseRedisConnection dbcnn = null;
        
        try {
            dbcnn = pool.borrowObjectFromPool();
            Jedis jedis = dbcnn.getConnection();
            Set<String> gameIDList = jedis.keys(log.getSession());
            Iterator<String> ite = gameIDList.iterator();
            while(ite.hasNext()){
                String gameID = ite.next();
                String infoGame = jedis.hget(log.getSession(), gameID);
                ScoreValueWrapper scoreValue = ScoreValueWrapper.parse(infoGame);
                long currentScore = scoreValue.getScore();
                PointCalculation pointCal = new ReCalculation(currentScore,scoreValue.getLatestLogin(),log.getTimeStamp());
                long newScore = pointCal.calculatePoint();
                
                // Set score again
                scoreValue.setScore(newScore);
                
                jedis.hset(log.getSession(), gameID, scoreValue.toJSONString());
                        
            }
        } catch (PoolException ex) {
            logger.error(ex);
        } catch (CalculationException ex) {
            logger.error(ex);
        } finally{
            try {
                if(dbcnn != null)
                    pool.returnObjectToPool(dbcnn);
            } catch (PoolException ex) {
                logger.error(ex.getMessage());
            }
        }
        return false;
    }
    
}
