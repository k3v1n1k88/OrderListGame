/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;

import strategy.calculation.AddMoreScorePaymentCalculation;
import configuration.ConfigConnectionPool;
import configuration.ConfigDatabaseLevelDB;
import configuration.ConfigFactory;
import configuration.ConfigSystem;
import configuration.ConfigScribe;
import database.connection.DatabaseConnectionPool;
import database.connection.DatabaseConnectionPoolLevelDB;
import database.connection.DatabaseConnectionPoolRedis;
import database.connection.DatabaseLevelDBConnection;
import database.connection.DatabaseLevelDBConnectionFactory;
import database.connection.DatabaseRedisConnection;
import database.connection.DatabaseRedisConnectionFactory;
import exception.CalculationException;
import exception.ConfigException;
import exception.DatabaseException;
import exception.PoolException;
import java.util.ArrayList;
import object.log.LogPayment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import object.value.database.MappingValueWrapper;
import object.value.database.ScoreValueWrapper;
import org.apache.log4j.Logger;
import org.iq80.leveldb.DB;
import static org.iq80.leveldb.impl.Iq80DBFactory.asString;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;
import redis.clients.jedis.Jedis;
import zcore.utilities.ScribeServiceClient;

/**
 *
 * @author root
 */
public class WorkerLogPayment extends WorkerAbstract<LogPayment> {

    private static final Logger logger = Logger.getLogger(WorkerLogPayment.class);
    
    private static DatabaseLevelDBConnection dbcnnLevelDB;
    
    private static ConfigScribe configScribe;

    static {
        try {
            // Initialize connection pool LevelDB
            ConfigConnectionPool configPoolLevelDB = configPool;
            ConfigDatabaseLevelDB configDatabaseLevelDB = ConfigFactory.getConfigDatabaseLevelDB(constant.PathConstant.PATH_TO_DATABASE_LEVELDB_CONFIG_FILE);
            dbcnnLevelDB = new DatabaseLevelDBConnection(configDatabaseLevelDB);
            dbcnnLevelDB.createConnection();
            
            configScribe = ConfigFactory.getConfigScribe(constant.PathConstant.PATH_TO_SCRIBE_CONFIG_FILE);
        } catch (ConfigException ex) {
            logger.error("Cannot create worker", ex);
        } catch (DatabaseException ex) {
            logger.info("Cannot create connection to database level DB", ex);
        }
    }

    public WorkerLogPayment(LogPayment log) {
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
            String session = jedis.hget(log.getUserID(), log.getGameID());

            // If mapping missing
            if (session == null) {
                // Write log sribe
                boolean res = ScribeServiceClient.getInstance(configScribe.getHost(),
                        configScribe.getPort(),
                        configScribe.getHost(),
                        configScribe.getPort(),
                        configScribe.getMaxConnection(),
                        configScribe.getMaxConnectionPerHost(),
                        configScribe.getInitConnection(),
                        configScribe.getTimeout()
                ).writeLog2(configScribe.getCategory(), this.log.parse2String());
                if(res == false){
                    logger.error("Cannot write to scribe"
                            +"\n"+log.parse2String());
                }
            } else {
                // Update database Scoring
                jedis.select(constant.DBConstantString.DATABASE_SCORING_INDEX);
                String infoScoring = jedis.hget(session, log.getGameID());
                
                // This case maybe never come true, because if you got session, that mean
                // database scoring have info about <session,gameID>. If everything happend, 
                // maybe something were wrong with my code when processing log login
                if (infoScoring == null) {
                    // Check mylog
                    logger.error("Something happened over my mind. Please check it again. Detail: "
                            + "\nsession = "+session
                            + "\ngameID = "+log.getGameID());
                } else {

                    ScoreValueWrapper scoreWrapper = ScoreValueWrapper.parse(infoScoring);

                    long amountTotal = scoreWrapper.getAmountTotal();
                    AddMoreScorePaymentCalculation calculation = new AddMoreScorePaymentCalculation(scoreWrapper.getScore(),
                            scoreWrapper.getLatestLogin(),
                            log.getTime(),
                            log.getAmount());
                    long newScore = calculation.calculatePoint();

                    scoreWrapper.setScore(newScore);
                    scoreWrapper.setAmountTotal(amountTotal + log.getAmount());
                    scoreWrapper.setLatestLogin(log.getTime());

                    jedis.hset(session, log.getGameID(), scoreWrapper.toJSONString());

                    // Write to database MappedLogPayment
//                    dbLevelDBcnn = dbcnnLevelDB.borrowObjectFromPool();
//                    dbcnnLevelDB.createConnection();
                    DB db = dbcnnLevelDB.getConnection();

                    String info = asString(db.get(bytes(session)));
                    MappingValueWrapper mappingValue;

                    // If session is not exist
                    if (info != null) {

                        mappingValue = MappingValueWrapper.parse(info);

                        // Create new info deposit
                        MappingValueWrapper.Info infoDeposit = new MappingValueWrapper.Info(log.getTime(), log.getAmount());

                        // Push info deposit into history deposit of this gameID
                        mappingValue.getListGameID().get(log.getGameID()).add(infoDeposit);

                        db.put(bytes(session), bytes(mappingValue.toJSONString()));

                        return true;

                    } // If session is existed
                    else {
                        // Create new info deposit
                        MappingValueWrapper.Info infoDeposit = new MappingValueWrapper.Info(log.getTime(), log.getAmount());

                        // Add into history deposit
                        List<MappingValueWrapper.Info> historyDeposit = new ArrayList<>();
                        historyDeposit.add(infoDeposit);

                        // Add info listgameID
                        Map<String, List<MappingValueWrapper.Info>> listGameID = new HashMap<>();
                        listGameID.put(log.getGameID(), historyDeposit);

                        // Push data into database Mapped LogPayment
                        MappingValueWrapper mappedPaymentValue = new MappingValueWrapper(session, listGameID);
                        db.put(bytes(session), bytes(mappedPaymentValue.toJSONString()));

                        return true;

                    }
                }
            }
        } catch (PoolException ex) {
            logger.error(ex.getMessage(),ex);
            ScribeServiceClient.getInstance(configScribe.getHost(),
                        configScribe.getPort(),
                        configScribe.getHost(),
                        configScribe.getPort(),
                        configScribe.getMaxConnection(),
                        configScribe.getMaxConnectionPerHost(),
                        configScribe.getInitConnection(),
                        configScribe.getTimeout()
                ).writeLog2("LogPaymentErrorByPool", this.log.parse2String());
        } catch (CalculationException ex) {
            logger.error(ex.getMessage(),ex);
            ScribeServiceClient.getInstance(configScribe.getHost(),
                        configScribe.getPort(),
                        configScribe.getHost(),
                        configScribe.getPort(),
                        configScribe.getMaxConnection(),
                        configScribe.getMaxConnectionPerHost(),
                        configScribe.getInitConnection(),
                        configScribe.getTimeout()
                ).writeLog2("LogPaymentErrorByCalculation", this.log.parse2String());
        } catch (DatabaseException ex) {
            logger.error(ex.getMessage(),ex);
            ScribeServiceClient.getInstance(configScribe.getHost(),
                        configScribe.getPort(),
                        configScribe.getHost(),
                        configScribe.getPort(),
                        configScribe.getMaxConnection(),
                        configScribe.getMaxConnectionPerHost(),
                        configScribe.getInitConnection(),
                        configScribe.getTimeout()
                ).writeLog2("LogPaymentErrorMapped", this.log.parse2String());
        } finally {
            try {
                if (dbRediscnn != null) {
                    poolRedis.returnObjectToPool(dbRediscnn);
                }
                if (dbcnnLevelDB != null) {
//                    try {
//                        dbcnnLevelDB.close();
//                    } catch (DatabaseException ex) {
//                        logger.error("Cannot close after write", ex);
//                    }
                }
            } catch (PoolException ex) {
                logger.error(ex.getMessage());
            }
        }
        return false;
    }

}
