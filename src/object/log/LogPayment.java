/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.log;

import exception.ParseLogException;
import com.google.gson.JsonObject;
import configuration.ConfigConnectionPool;
import configuration.ConfigDatabaseLevelDB;
import configuration.ConfigFactory;
import database.connection.DatabaseConnection;
import database.connection.DatabaseLevelDBConnection;
import database.connection.DatabaseRedisConnection;
import exception.CalculationException;
import exception.ConfigException;
import exception.DatabaseException;
import exception.PoolException;
import java.util.ArrayList;
import java.util.Date;
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
import strategy.calculation.AddMoreScorePaymentCalculation;
import zcore.utilities.ScribeServiceClient;

/**
 *
 * @author root
 */
public class LogPayment implements Log {

    private static final long serialVersionUID = 0_0_1L;

    private static final Logger logger = Logger.getLogger(LogPayment.class);
    private static DatabaseLevelDBConnection dbcnnLevelDB;

    static {
        try {
            ConfigDatabaseLevelDB configDatabaseLevelDB = ConfigFactory.getConfigDatabaseLevelDB(constant.PathConstant.PATH_TO_DATABASE_LEVELDB_CONFIG_FILE);

            dbcnnLevelDB = new DatabaseLevelDBConnection(configDatabaseLevelDB);
            dbcnnLevelDB.createConnection();

        } catch (ConfigException ex) {
            logger.error("Cannot create worker", ex);
        } catch (DatabaseException ex) {
            logger.info("Cannot create connection to database level DB", ex);
        }
    }

    private String userID;
    private String gameID;
    private long amount;
    private long timeStamp;

    public LogPayment() {
        this("", "", 0);
    }

    public LogPayment(String userID, String gameID, long amount) {
        this(userID, gameID, amount, new Date().getTime());
    }

    public LogPayment(String userID, String gameID, String amountString) throws ParseLogException {
        try {
            this.userID = userID;
            this.gameID = gameID;
            this.amount = Long.parseLong(amountString);
            this.timeStamp = new Date().getTime();
        } catch (Exception ex) {
            throw new ParseLogException("Patter amount string is not right", ex);
        }
    }

    public LogPayment(String userID, String gameID, long amount, long timestamp) {
        this.userID = userID;
        this.gameID = gameID;
        this.amount = amount;
        this.timeStamp = timestamp;
    }

    public String getUserID() {
        return userID;
    }

    public String getGameID() {
        return gameID;
    }

    public long getAmount() {
        return amount;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String parse2String() {
        JsonObject obj = new JsonObject();
        obj.addProperty(constant.DBConstantString.USER_ID, this.userID);
        obj.addProperty(constant.DBConstantString.GAME_ID, this.gameID);
        obj.addProperty(constant.DBConstantString.AMOUNT, this.amount);
        obj.addProperty(constant.DBConstantString.TIMESTAMP, this.timeStamp);
        return obj.toString();
    }
    
    /**
     *
     * @param
     *      dbcnn: DatabaseConnectionRedis
     * @return:
     *      true: if mapping 
     *      false: if missing mapping
     * @throws DatabaseException 
     *      Throws DatabaseException when you pass into DatabaseConnection is not DatabaseRedisConnection
     *      or when have error happened when calculation or something over my mind
     */
    @Override
    public boolean access(DatabaseConnection dbcnn) throws DatabaseException {
        if (dbcnn instanceof DatabaseRedisConnection) {
            DatabaseRedisConnection dbcnnRedis = (DatabaseRedisConnection) dbcnn;
            try {
                Jedis jedis = dbcnnRedis.getConnection();

                // Mapping
                jedis.select(constant.DBConstantString.DATABASE_MAPPING_GAMEID_SESSION_INDEX);
                String session = jedis.hget(this.userID, this.gameID);

                // If mapping missing
                if (session == null) {
                    return false;
                } else {
                    // Update database Scoring
                    jedis.select(constant.DBConstantString.DATABASE_SCORING_INDEX);
                    String infoScoring = jedis.hget(session, this.gameID);

                    // This case maybe never come true, because if you got session, that mean
                    // database scoring have info about <session,gameID>. If everything happend, 
                    // maybe something were wrong with my code when processing log login
                    if (infoScoring == null) {
                        // Check mylog
                        throw new DatabaseException("Something happened over my mind. Please check it again. Detail: "
                                + "\nsession = " + session
                                + "\ngameID = " + this.gameID);
                    } else {

                        ScoreValueWrapper scoreWrapper = ScoreValueWrapper.parse(infoScoring);

                        long amountTotal = scoreWrapper.getAmountTotal();
                        AddMoreScorePaymentCalculation calculation = new AddMoreScorePaymentCalculation(scoreWrapper.getScore(),
                                scoreWrapper.getLatestLogin(),
                                this.timeStamp,
                                this.amount);
                        long newScore = calculation.calculatePoint();

                        scoreWrapper.setScore(newScore);
                        scoreWrapper.setAmountTotal(amountTotal + this.amount);
                        scoreWrapper.setLatestLogin(this.timeStamp);

                        jedis.hset(session, this.gameID, scoreWrapper.toJSONString());

                        // Write to database MappedLogPayment
                        DB db = dbcnnLevelDB.getConnection();

                        String info = asString(db.get(bytes(session)));
                        MappingValueWrapper mappingValue;

                        // If session is not exist
                        if (info != null) {

                            mappingValue = MappingValueWrapper.parse(info);

                            // Create new info deposit
                            MappingValueWrapper.Info infoDeposit = new MappingValueWrapper.Info(this.timeStamp, this.amount);

                            // Push info deposit into history deposit of this gameID
                            mappingValue.getListGameID().get(this.gameID).add(infoDeposit);

                            db.put(bytes(session), bytes(mappingValue.toJSONString()));

                            return true;

                        } // If session is existed
                        else {
                            // Create new info deposit
                            MappingValueWrapper.Info infoDeposit = new MappingValueWrapper.Info(this.timeStamp, this.amount);

                            // Add into history deposit
                            List<MappingValueWrapper.Info> historyDeposit = new ArrayList<>();
                            historyDeposit.add(infoDeposit);

                            // Add info listgameID
                            Map<String, List<MappingValueWrapper.Info>> listGameID = new HashMap<>();
                            listGameID.put(this.gameID, historyDeposit);

                            // Push data into database Mapped LogPayment
                            MappingValueWrapper mappedPaymentValue = new MappingValueWrapper(session, listGameID);
                            db.put(bytes(session), bytes(mappedPaymentValue.toJSONString()));

                            return true;

                        }
                    }
                }
            } catch (CalculationException ex) {
                throw new DatabaseException("Error happened when calculation", ex);
            } 
        } else {
            throw new DatabaseException("Cannot access with a connection is not DatabaseRedisConnection");
        }
    }

}
