/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.log;

import com.google.gson.JsonObject;
import database.connection.DatabaseConnection;
import database.connection.DatabaseRedisConnection;
import exception.CalculationException;
import exception.DatabaseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import object.value.database.ScoreValueWrapper;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import strategy.calculation.PointCalculation;
import strategy.calculation.ReCalculation;

/**
 *
 * @author root
 */
public class LogLandingPage implements Log {
    
    private static final Logger logger = Logger.getLogger(LogLandingPage.class);

    private String session;
    private long timeStamp;

    public LogLandingPage(String session) {
        this.session = session;
        this.timeStamp = new Date().getTime();
    }

    public LogLandingPage() {
        this("");
    }

    public String getSession() {
        return session;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String parse2String() {
        JsonObject obj = new JsonObject();
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
                Set<String> gameIDList = jedis.keys(this.session);
                Iterator<String> ite = gameIDList.iterator();
                while (ite.hasNext()) {

                    String gameIDElement = ite.next();
                    String infoGame = jedis.hget(this.session, gameIDElement);

                    // Calculate new score
                    ScoreValueWrapper scoreValue = ScoreValueWrapper.parse(infoGame);
                    long currentScore = scoreValue.getScore();
                    PointCalculation pointCal = new ReCalculation(currentScore, scoreValue.getLatestLogin(),
                            this.timeStamp);
                    long newScore = pointCal.calculatePoint();

                    // Modify score
                    scoreValue.setScore(newScore);

                    jedis.hset(this.session, gameIDElement, scoreValue.toJSONString());
                    
                    return true;
                }
            } catch (CalculationException ex) {
                throw new DatabaseException("Error happened when calculation", ex);
            }
            return false;
        } else {
            throw new DatabaseException("Cannot access with a connection is not DatabaseRedisConnection");
        }
    }

}
