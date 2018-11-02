/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.log;

import error.code.ParserErrorCode;
import exception.ParseLogException;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author root
 */
public class LogParser {
    
    private static final Logger LOGGER = Logger.getLogger(LogParser.class);
    
    public LogParser(){
    
    }
    
    public LogLogin toLogLogin(String log) throws ParseException, ParseLogException{
                
        LogLogin logLogin = null;
        JSONObject jsonObj = null;
        
        JSONParser jsonParser = new JSONParser();
        try {
            jsonObj = (JSONObject) jsonParser.parse(log);
            
            if(!jsonObj.containsKey(constant.DBConstantString.USER_ID)
                    ||!jsonObj.containsKey(constant.DBConstantString.GAME_ID)
                    ||!jsonObj.containsKey(constant.DBConstantString.SESSION)){
                throw new ParseLogException(ParserErrorCode.MISSING_1);
            }
            String session = String.valueOf(jsonObj.get(constant.DBConstantString.SESSION));
            String userID = String.valueOf(jsonObj.get(constant.DBConstantString.USER_ID));
            String gameID = String.valueOf(jsonObj.get(constant.DBConstantString.GAME_ID));
            
//            long timeStamp = 0;
//            
//            if(jsonObj.containsKey(Constant.DBConstantString.TIMESTAMP)){
//                timeStamp = Long.parseLong((String) jsonObj.get(Constant.DBConstantString.TIMESTAMP));
//            }
//            else{
//                timeStamp = new Date().getTime();
//            }
            
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
    
    public LogPayment toLogPayment(String log) throws ParseException, ParseLogException{
        LogPayment logPayment = null;
        JSONObject jsonObj = null;
        
        JSONParser jsonParser = new JSONParser();
        try {
            jsonObj = (JSONObject) jsonParser.parse(log);
            if(!jsonObj.containsKey(constant.DBConstantString.USER_ID)
                    ||!jsonObj.containsKey(constant.DBConstantString.GAME_ID)
                    ||!jsonObj.containsKey(constant.DBConstantString.AMOUNT)){
                
                throw new ParseLogException(ParserErrorCode.MISSING_2_1);
            
            }
            try{
                String userID = String.valueOf(jsonObj.get(constant.DBConstantString.USER_ID));
                String gameID = String.valueOf(jsonObj.get(constant.DBConstantString.GAME_ID));
                long amount =  Long.parseLong(String.valueOf(jsonObj.get("amount")));
                
//                long timestamp = 0;
//            
//                if(jsonObj.containsKey(Constant.DBConstantString.TIMESTAMP)){
//                    timestamp = Long.parseLong((String) jsonObj.get(Constant.DBConstantString.TIMESTAMP));
//                }
//                else{
//                    timestamp = new Date().getTime();
//                }
                
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
}
