/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.log;


import exception.ParseLogException;
import com.google.gson.JsonObject;
import java.util.Date;
import org.apache.log4j.Logger;

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
    private long time;
    
    public LogPayment(){
        this("","",0);
    }
    
    public LogPayment(String userID, String gameID, long amount){
        this(userID,gameID,amount,new Date().getTime());
    }
    
    public LogPayment(String userID, String gameID, String amountString) throws ParseLogException{
        try{
            this.userID = userID;
            this.gameID = gameID;
            this.amount = Long.parseLong(amountString);
            this.time = new Date().getTime();
        }catch(Exception ex){
            throw new ParseLogException("Patter amount string is not right",ex);
        }        
    }
    
    public LogPayment(String userID, String gameID, long amount,long timestamp){
        this.userID = userID;
        this.gameID = gameID;
        this.amount = amount;
        this.time = timestamp;
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

    public long getTime() {
        return time;
    }
    
    @Override
    public String parse2String() {
        JsonObject obj = new JsonObject();
        obj.addProperty(constant.DBConstantString.USER_ID, this.userID);
        obj.addProperty(constant.DBConstantString.GAME_ID, this.gameID);
        obj.addProperty(constant.DBConstantString.AMOUNT, this.amount);
        obj.addProperty(constant.DBConstantString.TIMESTAMP, this.time);
        return obj.toString();
    }

}
