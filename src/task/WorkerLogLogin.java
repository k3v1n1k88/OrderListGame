/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;

import strategy.calculation.AddMoreScoreLoginCalculation;
import strategy.calculation.PointCalculation;
import configuration.ConfigConnectionPool;
import configuration.ConfigSystem;
import constant.SystemConstant;
import database.connection.DatabaseConnectionPool;
import database.connection.DatabaseConnectionPoolRedis;
import database.connection.DatabaseRedisConnection;
import database.connection.DatabaseRedisConnectionFactory;
import exception.CalculationException;
import exception.ConfigException;
import exception.PoolException;
import object.log.LogLogin;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import object.value.database.ScoreValueWrapper;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 *
 * @author root
 */
public class WorkerLogLogin extends WorkerAbstract<LogLogin> {
    
    private static final Logger logger = Logger.getLogger(WorkerLogLogin.class);
    
    public WorkerLogLogin(LogLogin log){
        super(log);
    }

    @Override
    protected boolean processLog() {
        DatabaseRedisConnection dbcnn = null;
        try {
            // Get session in DB
            dbcnn = poolRedis.borrowObjectFromPool();
            Jedis jedis = dbcnn.getConnection();
            String info = jedis.hget(log.getSession(), log.getGameID());
            
            // If gameID is not exist in Scoring database accroding to this session
            if(info == null){
                // Access with database Scoring
                ScoreValueWrapper infoWrapper = new ScoreValueWrapper(SystemConstant.INIT_SCORE, 1, 0, log.getTime());
                jedis.hset(log.getSession(), log.getGameID(), infoWrapper.toJSONString());
                
                // Get all list gameID of this session
                Set<String> listGameID = jedis.hkeys(log.getSession());
                
                // Access with database Recommendation
                jedis.select(constant.DBConstantString.DATABASE_RECOMMENDATION_INDEX);
                
                if(listGameID != null){
                    // Traverse all gameID to add more point
                    Iterator<String> ite = listGameID.iterator();
                    while(ite.hasNext()){
                        String gameID = ite.next();
                        jedis.sadd(constant.DBConstantString.LIST_GAME, gameID);
                        
                        // Get current point
                        String currentPointString = jedis.hget((constant.DBConstantString.GAME_ID+":").concat(gameID), constant.DBConstantString.POINT);
                        
                        // Point string can be null if gameID is not exist in Recommendation DB 
                        // (otherway, gameID is new game)
                        
                        Map<String, String> gameIDProp = new HashMap<>();
                        
                        if(currentPointString != null){
                            long point = Long.valueOf(currentPointString); 
                            // Increase point
                            point = point + 1;
                            gameIDProp.put(constant.DBConstantString.POINT, String.valueOf(point));
                        }
                        else{
                            gameIDProp.put(constant.DBConstantString.POINT, String.valueOf(listGameID.size()-1));
                        }
                        jedis.hmset((constant.DBConstantString.GAME_ID+":").concat(gameID), gameIDProp);
                    }
                }
                // Access with database Mapping
                jedis.select(constant.DBConstantString.DATABASE_MAPPING_GAMEID_SESSION_INDEX);
                jedis.hset(log.getUserID(), log.getGameID(), log.getSession());
                return false;
            }
            // If this gameID is exist in Scoring database, we just increse point in Scoring database
            // and dont care about this gameID in Recommendation DB
            else{
                
                ScoreValueWrapper scoreValue = ScoreValueWrapper.parse(info);
                
                long currentPoint = scoreValue.getScore();
                long latestLogin = scoreValue.getLatestLogin();
                long timesOfLogin = scoreValue.getTimesOfLogin();
                long currentTime = log.getTime();
                
                PointCalculation pc = new AddMoreScoreLoginCalculation(currentPoint,latestLogin, currentTime);
       
                long newPoint = pc.calculatePoint();
                
                scoreValue.setScore(newPoint);
                scoreValue.setTimesOfLogin(timesOfLogin+1);
                scoreValue.setLatestLogin(currentTime);
                
                jedis.hset(log.getSession(), log.getGameID(), scoreValue.toJSONString());
                
                return true;
            }
            
        } catch (PoolException ex) {
            logger.error(ex);
        } catch (CalculationException ex) {
            logger.error(ex);
        } finally{
            if(dbcnn!=null){
                try {
                    poolRedis.returnObjectToPool(dbcnn);
                } catch (PoolException ex) {
                    logger.error(ex.getMessage());
                }
            }
        }
        return false;
        
    }
}

