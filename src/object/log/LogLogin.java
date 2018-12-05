/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.log;


import com.google.gson.JsonObject;
import constant.SystemConstant;
import database.connection.DatabaseConnection;
import database.connection.DatabaseRedisConnection;
import exception.CalculationException;
import exception.DatabaseException;
import exception.PoolException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import object.value.database.ScoreValueWrapper;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;
import strategy.calculation.AddMoreScoreLoginCalculation;
import strategy.calculation.PointCalculation;

/**
 *
 * @author root
 */
public class LogLogin implements Log {

    private static final long serialVersionUID = 0L;

    private static final Logger logger = Logger.getLogger(LogLogin.class);

    private String session;
    private String userID;
    private String gameID;
    private long timeStamp;

    public LogLogin() {
        this("", "", "");
    }

    public LogLogin(String session, String userID, String gameID) {
        this(session, userID, gameID, new Date().getTime());
    }

    public LogLogin(String session, String userID, String gameID, long timeStamp) {
        this.session = session;
        this.userID = userID;
        this.gameID = gameID;
        this.timeStamp = timeStamp;
    }

    public String getSession() {
        return session;
    }

    public String getUserID() {
        return userID;
    }

    public String getGameID() {
        return gameID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String parse2String() {
        JsonObject obj = new JsonObject();
        obj.addProperty(constant.DBConstantString.USER_ID, this.userID);
        obj.addProperty(constant.DBConstantString.GAME_ID, this.gameID);
        obj.addProperty(constant.DBConstantString.SESSION, this.session);
        obj.addProperty(constant.DBConstantString.TIMESTAMP, this.timeStamp);
        return obj.toString();
    }

    @Override
    public boolean access(DatabaseConnection dbcnn) throws DatabaseException {
        if (dbcnn instanceof DatabaseRedisConnection) {
            DatabaseRedisConnection dbcnnRedis = (DatabaseRedisConnection) dbcnn;
            try {
                Jedis jedis = dbcnnRedis.getConnection();
                String info = jedis.hget(this.session, this.gameID);

                // If gameID is not exist in Scoring database accroding to this session
                if (info == null) {
                    // Access with database Scoring
                    ScoreValueWrapper infoWrapper = new ScoreValueWrapper(SystemConstant.INIT_SCORE, 
                            1, 0, this.timeStamp);
                    jedis.hset(this.session, this.gameID, infoWrapper.toJSONString());

                    // Get all list gameID of this session
                    Set<String> listGameID = jedis.hkeys(this.session);

                    // Access with database Recommendation
                    jedis.select(constant.DBConstantString.DATABASE_RECOMMENDATION_INDEX);

                    if (listGameID != null) {
                        // Traverse all gameID to add more point
                        Iterator<String> ite = listGameID.iterator();
                        while (ite.hasNext()) {
                            String gameIDElement = ite.next();
                            jedis.sadd(constant.DBConstantString.LIST_GAME, gameIDElement);

                            // Get current point
                            String currentPointString = jedis.hget((constant.DBConstantString.GAME_ID + ":")
                                    .concat(gameIDElement), constant.DBConstantString.POINT);

                            // Point string can be null if gameID is not exist in Recommendation DB 
                            // (thinking same as gameID is new game in Recommendation DB)
                            Map<String, String> gameIDProp = new HashMap<>();

                            if (currentPointString != null) {
                                long currentPoint = Long.valueOf(currentPointString);
                                // Increase point
                                if (!gameIDElement.equals(this.gameID)) {
                                    currentPoint = currentPoint + 1;
                                } else {
                                    currentPoint = currentPoint + listGameID.size() - 1;
                                }
                                gameIDProp.put(constant.DBConstantString.POINT, String.valueOf(currentPoint));
                            } else {
                                gameIDProp.put(constant.DBConstantString.POINT, String.valueOf(listGameID.size() - 1));
                            }
                            jedis.hmset((constant.DBConstantString.GAME_ID + ":").concat(gameIDElement), gameIDProp);
                        }
                    } else {
                        throw new DatabaseException("Some thing happened over my mind. Please check it again:" 
                                + this.parse2String());
                    }
                    // Access with database Mapping
                    jedis.select(constant.DBConstantString.DATABASE_MAPPING_GAMEID_SESSION_INDEX);
                    jedis.hset(this.userID, this.gameID, this.session);
                    return false;
                } // If this gameID is existed in Scoring database, we just increse point in Scoring database
                // and dont care about this gameID in Recommendation DB
                else {

                    ScoreValueWrapper scoreValue = ScoreValueWrapper.parse(info);

                    long currentPoint = scoreValue.getScore();
                    long latestLogin = scoreValue.getLatestLogin();
                    long timesOfLogin = scoreValue.getTimesOfLogin();
                    long currentTime = this.timeStamp;

                    PointCalculation pc = new AddMoreScoreLoginCalculation(currentPoint, latestLogin, currentTime);

                    long newPoint = pc.calculatePoint();

                    scoreValue.setScore(newPoint);
                    scoreValue.setTimesOfLogin(timesOfLogin + 1);
                    scoreValue.setLatestLogin(currentTime);

                    jedis.hset(this.session, this.gameID, scoreValue.toJSONString());

                    return true;
                }

            } catch (CalculationException ex) {
                throw new DatabaseException("Error happened when calculation", ex);
            }

        } else {
            throw new DatabaseException("Cannot access with a connection is not DatabaseRedisConnection");
        }
    }
}
