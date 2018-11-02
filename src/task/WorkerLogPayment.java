/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;

import strategy.calculation.AddMoreScorePaymentCalculation;
import configuration.ConfigConnectionPool;
import configuration.ConfigOfSystem;
import database.connection.DatabaseConnectionPoolLevelDB;
import database.connection.DatabaseConnectionPoolRedis;
import database.connection.DatabaseLevelDBConnection;
import database.connection.DatabaseLevelDBConnectionFactory;
import database.connection.DatabaseRedisConnection;
import database.connection.DatabaseRedisConnectionFactory;
import exception.ConfigException;
import exception.PoolException;
import object.log.LogPayment;
import java.util.HashMap;
import java.util.Map;
import object.value.database.MappingValueWrapper;
import object.value.database.ScoreValueWrapper;
import org.apache.log4j.Logger;
import org.iq80.leveldb.DB;
import static org.iq80.leveldb.impl.Iq80DBFactory.asString;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;
import redis.clients.jedis.Jedis;

/**
 *
 * @author root
 */
public class WorkerLogPayment extends WorkerAbstract<LogPayment>{

    private static final Logger logger = Logger.getLogger(WorkerLogPayment.class);
    
    private static DatabaseConnectionPoolRedis poolRedis;
    private static DatabaseConnectionPoolLevelDB poolLevelDB;
    
    private static ConfigOfSystem configSystem;
    
    static{
        try {
            // Read config of system
            configSystem = new ConfigOfSystem();
            
            // Initialize connection pool Redis
            ConfigConnectionPool configPoolRedis = new ConfigConnectionPool();
            DatabaseRedisConnectionFactory factoryRedis = new DatabaseRedisConnectionFactory();
            poolRedis = new DatabaseConnectionPoolRedis(factoryRedis,configPoolRedis);
            
            // Initialize connection pool LevelDB
            ConfigConnectionPool configPoolLevelDB = new ConfigConnectionPool();
            DatabaseLevelDBConnectionFactory factoryLevelDB = new DatabaseLevelDBConnectionFactory(constant.DBConstantString.DATABASE_MAPPING_NAME);
            poolLevelDB = new DatabaseConnectionPoolLevelDB(factoryLevelDB,configPoolLevelDB);
            
        } catch (ConfigException ex) {
            logger.info(ex.getMessage());
            System.exit(0);
        }
    }
    
    public WorkerLogPayment(LogPayment log){
        super(log);
    }

    @Override
    protected boolean processLog() {
        DatabaseRedisConnection dbRediscnn = null;
        DatabaseLevelDBConnection dbLevelDBcnn = null;
        try {
            // Get connection
            dbRediscnn = poolRedis.borrowObjectFromPool();
            Jedis jedis = dbRediscnn.getConnection();
           
            // Mapping
            jedis.select(constant.DBConstantString.DATABASE_MAPPING_GAMEID_SESSION_INDEX);
            String session = jedis.get(log.getUserID());
            
            if(session == null){
                // Write log sribe
            }
            else{
                // Update database Scoring
                jedis.select(constant.DBConstantString.DATABASE_SCORING_INDEX);
                String infoScoring = jedis.hget(session, log.getGameID());
                ScoreValueWrapper scoreWrapper = ScoreValueWrapper.parse(infoScoring);
                
                long amountTotal  = scoreWrapper.getAmountTotal();
                AddMoreScorePaymentCalculation calculation = new AddMoreScorePaymentCalculation(scoreWrapper.getScore(),scoreWrapper.getLatestLogin(),log.getTime(),log.getAmount());
                long newScore = calculation.calculatePoint();
                
                scoreWrapper.setScore(newScore);
                scoreWrapper.setAmountTotal(amountTotal+log.getAmount());
                scoreWrapper.setLatestLogin(log.getTime());
                
                jedis.hset(session, log.getGameID(), scoreWrapper.toJSONString());
                
                // Write to database Mapping
                dbLevelDBcnn = poolLevelDB.borrowObjectFromPool();
                DB db = dbLevelDBcnn.getConnection();
                
                String info = asString(db.get(bytes(session)));
                MappingValueWrapper mappingValue;
                
                if(info != null){
                    mappingValue = MappingValueWrapper.parse(info);
                    MappingValueWrapper.Info infoDesposite = mappingValue.getListGameID().get(log.getGameID());
                    infoDesposite.getDeposit().put(log.getTime(), log.getAmount());
                    mappingValue.getListGameID().get(log.getGameID()).getDeposit().put(log.getTime(), log.getAmount());
                    
                    db.put(bytes(session), bytes(mappingValue.toJSONString()));
                    return true;
                }
                else{
                    Map<Long,Long> despositList = new HashMap<>();
                    despositList.put(log.getTime(), log.getAmount());
                    MappingValueWrapper.Info infoDeposit = new MappingValueWrapper.Info(despositList);
                    Map<String,MappingValueWrapper.Info> listGame = new HashMap<>();
                    listGame.put(log.getGameID(), infoDeposit);
                    
                    mappingValue = new MappingValueWrapper(log.getUserID(),listGame);
                    db.put(bytes(session), bytes(mappingValue.toJSONString()));
                    return true;
                }
            }
        } catch (PoolException ex) {
            logger.error(ex);
        } finally{
            try {
                if(dbRediscnn != null)
                    poolRedis.returnObjectToPool(dbRediscnn);
                if(dbLevelDBcnn != null)
                    poolLevelDB.returnObjectToPool(dbLevelDBcnn);
            } catch (PoolException ex) {
                logger.error(ex.getMessage());
            }
        }
        return false;
    }
    
}
