/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import ErrorCode.ParserErrorCode;
import Exception.ParseLogException;
import com.google.gson.JsonObject;
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

    public LogLogin(){
        this("","","");
    }
    
    public LogLogin(String session, String userID, String gameID){
        this.session = session;
        this.userID = userID;
        this.gameID = gameID;
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
    
    public static LogLogin logToObj(String log) throws ParseException, ParseLogException{
        
        LogLogin logLogin = null;
        JSONObject jsonObj = null;
        
        JSONParser jsonParser = new JSONParser();
        try {
            jsonObj = (JSONObject) jsonParser.parse(log);
            
            if(!jsonObj.containsKey(Constant.DBConstantString.USERID_STRING)
                    ||!jsonObj.containsKey(Constant.DBConstantString.GAMEID_STRING)
                    ||!jsonObj.containsKey(Constant.DBConstantString.SESSION_STRING)){
                throw new ParseLogException(ParserErrorCode.MISSING_1);
            }
            String session = String.valueOf(jsonObj.get(Constant.DBConstantString.SESSION_STRING));
            String userID = String.valueOf(jsonObj.get(Constant.DBConstantString.USERID_STRING));
            String gameID = String.valueOf(jsonObj.get(Constant.DBConstantString.GAMEID_STRING));
            logLogin = new LogLogin(session,userID,gameID);  
            
        } catch (ParseException ex) {
            LOGGER.error("Error when parse log message to JSON. Check your log message and try it again.\nError detail:\n"+log+"\n"+ex);
            throw ex;
        } catch (ParseLogException ex) {
            LOGGER.error("Error when create LogPayment from log message. Check your log message and try it aganin.\nError detail:\n"+log+"\n"+ex);
            throw ex;
        }
        return logLogin;
    }
    

    @Override
    public String parse2String() {
        JsonObject obj = new JsonObject();
        obj.addProperty(Constant.DBConstantString.USERID_STRING, this.userID);
        obj.addProperty(Constant.DBConstantString.GAMEID_STRING, this.gameID);
        obj.addProperty(Constant.DBConstantString.SESSION_STRING, this.session);
        return obj.toString();
    }

    @Override
    public Log parse2Log(String infoLog) throws ParseException, ParseLogException {
        return LogLogin.logToObj(infoLog);
    }

}
