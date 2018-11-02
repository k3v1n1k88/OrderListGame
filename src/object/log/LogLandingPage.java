/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.log;

import com.google.gson.JsonObject;
import java.util.Date;

/**
 *
 * @author root
 */
public class LogLandingPage implements Log{

    private String session;
    private long timeStamp;
    
    
    public LogLandingPage(String session){
        this.session = session;
        this.timeStamp = new Date().getTime();
    }
    
    public LogLandingPage(){
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
        JsonObject obj =  new JsonObject();
        obj.addProperty(constant.DBConstantString.SESSION, this.session);
        obj.addProperty(constant.DBConstantString.TIMESTAMP, this.timeStamp);
        return obj.toString();
    }
    
}
