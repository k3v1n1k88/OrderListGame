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
import java.util.ArrayList;
import object.log.LogPayment;
import java.util.HashMap;
import java.util.List;
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
                if(infoScoring == null){
                    // User deposite for gameID never login
                }
                else{
                    
                    ScoreValueWrapper scoreWrapper = ScoreValueWrapper.parse(infoScoring);

                    long amountTotal  = scoreWrapper.getAmountTotal();
                    AddMoreScorePaymentCalculation calculation = new AddMoreScorePaymentCalculation(scoreWrapper.getScore(),scoreWrapper.getLatestLogin(),log.getTime(),log.getAmount());
                    long newScore = calculation.calculatePoint();

                    scoreWrapper.setScore(newScore);
                    scoreWrapper.setAmountTotal(amountTotal+log.getAmount());
                    scoreWrapper.setLatestLogin(log.getTime());

                    jedis.hset(session, log.getGameID(), scoreWrapper.toJSONString());

                    // Write to database MappedLogPayment
                    dbLevelDBcnn = poolLevelDB.borrowObjectFromPool();
                    DB db = dbLevelDBcnn.getConnection();

                    String info = asString(db.get(bytes(session)));
                    MappingValueWrapper mappingValue;
                    
                    // If session is not exist
                    if(info != null){
                        
                        mappingValue = MappingValueWrapper.parse(info);
                        
                        // Create new info deposit
                        MappingValueWrapper.Info infoDeposit = new MappingValueWrapper.Info(log.getTime(),log.getAmount());
                        
                        // Push info deposit into history deposit of this gameID
                        mappingValue.getListGameID().get(log.getGameID()).add(infoDeposit);

                        db.put(bytes(session), bytes(mappingValue.toJSONString()));
                        
                        return true;
                        
                    }
                    // If session is existed
                    else{
                        // Create new info deposit
                        MappingValueWrapper.Info infoDeposit = new MappingValueWrapper.Info(log.getTime(),log.getAmount());
                        
                        // Add into history deposit
                        List<MappingValueWrapper.Info> historyDeposit = new ArrayList<>();
                        historyDeposit.add(infoDeposit);
                        
                        // Add info listgameID
                        Map<String,List<MappingValueWrapper.Info>> listGameID = new HashMap<>();
                        listGameID.put(log.getGameID(), historyDeposit);
                        
                        // Push data into database Mapped LogPayment
                        MappingValueWrapper mappedPaymentValue = new MappingValueWrapper(session,listGameID);
                        db.put(bytes(session), bytes(mappedPaymentValue.toJSONString()));
                        
                        return true;
                        
                    }
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
