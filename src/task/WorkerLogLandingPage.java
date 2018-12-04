/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;

import strategy.calculation.PointCalculation;
import strategy.calculation.ReCalculation;
import configuration.ConfigConnectionPool;
import configuration.ConfigFactory;
import configuration.ConfigSystem;
import database.connection.DatabaseConnectionPool;
import database.connection.DatabaseConnectionPoolRedis;
import database.connection.DatabaseRedisConnection;
import database.connection.DatabaseRedisConnectionFactory;
import exception.CalculationException;
import exception.ConfigException;
import exception.PoolException;
import object.log.LogLandingPage;
import java.util.Iterator;
import java.util.Set;
import object.value.database.ScoreValueWrapper;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 *
 * @author root
 */
public class WorkerLogLandingPage extends WorkerAbstract<LogLandingPage>{

    private static final Logger logger = Logger.getLogger(WorkerLogLandingPage.class);
    
    public WorkerLogLandingPage(LogLandingPage log){
        super(log);
    }
    
    @Override
    protected boolean processLog() {
        DatabaseRedisConnection dbcnn = null;
        
        try {
            dbcnn = poolRedis.borrowObjectFromPool();
            Jedis jedis = dbcnn.getConnection();
            Set<String> gameIDList = jedis.keys(log.getSession());
            Iterator<String> ite = gameIDList.iterator();
            while(ite.hasNext()){
                
                String gameID = ite.next();
                String infoGame = jedis.hget(log.getSession(), gameID);
                
                // Calculate new score
                ScoreValueWrapper scoreValue = ScoreValueWrapper.parse(infoGame);
                long currentScore = scoreValue.getScore();
                PointCalculation pointCal = new ReCalculation(currentScore,scoreValue.getLatestLogin(),log.getTimeStamp());
                long newScore = pointCal.calculatePoint();
                
                // Modify score
                scoreValue.setScore(newScore);
                
                jedis.hset(log.getSession(), gameID, scoreValue.toJSONString());
                        
            }
        } catch (PoolException | CalculationException ex) {
            logger.error(ex);
        } finally{
            try {
                if(dbcnn != null)
                    poolRedis.returnObjectToPool(dbcnn);
            } catch (PoolException ex) {
                logger.error(ex.getMessage());
            }
        }
        return false;
    }
    
}
