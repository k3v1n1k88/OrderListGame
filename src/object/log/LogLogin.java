/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.log;

import error.code.ParserErrorCode;
import exception.ParseLogException;
import com.google.gson.JsonObject;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author root
 */
public class LogLogin implements Log{
    private static final long serialVersionUID = 0L;
    
    private static final Logger LOGGER = Logger.getLogger(LogLogin.class);
 
    private String session;
    private String userID;
    private String gameID;
    private long time;

    public LogLogin() {
        this("","","");
    }
    
    public LogLogin(String session, String userID, String gameID){
        this(session,userID,gameID,new Date().getTime());
    }
    
    public LogLogin(String session, String userID, String gameID,long time){
        this.session = session;
        this.userID = userID;
        this.gameID = gameID;
        this.time = time;
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

    public long getTime() {
        return time;
    }    

    @Override
    public String parse2String() {
        JsonObject obj = new JsonObject();
        obj.addProperty(constant.DBConstantString.USER_ID, this.userID);
        obj.addProperty(constant.DBConstantString.GAME_ID, this.gameID);
        obj.addProperty(constant.DBConstantString.SESSION, this.session);
        obj.addProperty(constant.DBConstantString.TIMESTAMP, this.time);
        return obj.toString();
    }
}
