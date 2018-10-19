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
public class LogPayment implements Log{
    
    private static final long serialVersionUID = 0_0_1L;
    
    private static final Logger LOGGER = Logger.getLogger(LogPayment.class);
            
    private String userID;
    private String gameID;
    private long amount;
    
    public String getUserID() {
        return userID;
    }

    public String getGameID() {
        return gameID;
    }

    public long getAmount() {
        return amount;
    }
    
    public LogPayment(){
        this("","",0);
    }
    
    public LogPayment(String userID, String gameID, long amount){
        this.userID = userID;
        this.gameID = gameID;
        this.amount = amount;
    }
    
    public static LogPayment logToObj(String log) throws ParseException, ParseLogException{
        LogPayment logPayment = null;
        JSONObject jsonObj = null;
        
        JSONParser jsonParser = new JSONParser();
        try {
            jsonObj = (JSONObject) jsonParser.parse(log);
            if(!jsonObj.containsKey(Constant.DBConstantString.USERID_STRING)
                    ||!jsonObj.containsKey(Constant.DBConstantString.GAMEID_STRING)
                    ||!jsonObj.containsKey(Constant.DBConstantString.AMOUNT_STRING)){
                
                throw new ParseLogException(ParserErrorCode.MISSING_2_1);
            
            }
            try{
                String userID = String.valueOf(jsonObj.get(Constant.DBConstantString.USERID_STRING));
                String gameID = String.valueOf(jsonObj.get(Constant.DBConstantString.GAMEID_STRING));
                long amount =  Long.parseLong(String.valueOf(jsonObj.get("amount")));
                
                logPayment = new LogPayment(userID,gameID,amount);
                
                return logPayment;
            } catch(NumberFormatException ex){
                throw new ParseLogException(ParserErrorCode.MISSING_2_2);
            }
        } catch (ParseException ex) {
            LOGGER.error("Error when parse log message to JSON. Check your log message and try it again.\nError detail:\n"+ex);
            throw ex;
        } catch (ParseLogException ex) {
            LOGGER.error("Error when create LogPayment from log message:\""+log+"\"\n. Check your log message and try it aganin.\nError detail:\n"+ex);
            throw ex;
        }
    }

    @Override
    public String parse2String() {
        JsonObject obj = new JsonObject();
        obj.addProperty(Constant.DBConstantString.USERID_STRING, this.userID);
        obj.addProperty(Constant.DBConstantString.GAMEID_STRING, this.gameID);
        obj.addProperty(Constant.DBConstantString.AMOUNT_STRING, this.amount);
        return obj.toString();
    }

    @Override
    public Log parse2Log(String infoLog) throws ParseException, ParseLogException {
        return LogPayment.logToObj(infoLog);
    }

}
