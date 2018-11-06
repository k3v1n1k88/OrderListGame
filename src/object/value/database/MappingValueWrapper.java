/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.value.database;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author root
 */
public class MappingValueWrapper {
    
    private String userID;
    private Map<String,List<Info>> listGameID;

    public static MappingValueWrapper parse(String jsonString){
        Gson gson = new Gson();
        MappingValueWrapper ret = gson.fromJson(jsonString, MappingValueWrapper.class);
        return ret;
    }

    public MappingValueWrapper(String userID, Map<String, List<Info>> listGameID) {
        this.userID = userID;
        this.listGameID = listGameID;
    }
    
    public String toJSONString(){
        return new Gson().toJson(this);
    }
    
    public static class Info{
        
        private long timestamp;
        private long amount;

        public Info(long timestamp, long amount) {
            this.timestamp = timestamp;
            this.amount = amount;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public long getAmount() {
            return amount;
        }
        
    }

    public String getUserID() {
        return userID;
    }

    public Map<String,List<Info>> getListGameID() {
        return this.listGameID;
    }
    
    public void setListGameID(Map<String, List<Info>> listGameID) {
        this.listGameID = listGameID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    
    
    public static void main(String[] args) {
        MappingValueWrapper.Info info1 = new MappingValueWrapper.Info(1000000L,13984948593L);
        MappingValueWrapper.Info info2 = new MappingValueWrapper.Info(1289211212L,13984948593L);
        List<Info> listInfo = new ArrayList<>();
        listInfo.add(info1);
        listInfo.add(info2);
        Map<String,List<Info>> m = new HashMap<>();
        m.put("176271627162", listInfo);
        MappingValueWrapper mappedPaymentValue = new MappingValueWrapper("34938944",m);
        System.out.println(mappedPaymentValue.toJSONString());
    }
}
