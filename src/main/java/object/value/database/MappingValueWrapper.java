/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.value.database;

import com.google.gson.Gson;
import java.util.Map;

/**
 *
 * @author root
 */
public class MappingValueWrapper {
    
    private String userID;
    private Map<String,Info> listGameID;

    public static MappingValueWrapper parse(String jsonString){
        Gson gson = new Gson();
        MappingValueWrapper ret = gson.fromJson(jsonString, MappingValueWrapper.class);
        return ret;
    }

    public MappingValueWrapper(String userID, Map<String, Info> listGameID) {
        this.userID = userID;
        this.listGameID = listGameID;
    }
    
    public String toJSONString(){
        return new Gson().toJson(this);
    }
    
    public static class Info{
        
        private Map<Long, Long> deposit;
        
        public Info(Map<Long,Long> deposit){
            this.deposit = deposit;
        }

        public Map<Long, Long> getDeposit() {
            return deposit;
        }

        public void setDeposit(Map<Long, Long> deposit) {
            this.deposit = deposit;
        }
        
    }

    public String getUserID() {
        return userID;
    }

    public Map<String, Info> getListGameID() {
        return listGameID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setListGameID(Map<String, Info> listGameID) {
        this.listGameID = listGameID;
    }
    
}
