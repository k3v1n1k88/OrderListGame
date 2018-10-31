/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author anonymous
 */
public class ApiOutput implements Serializable {

    private static final long serialVersionUID = 5933185764034287202L;

    private int returnCode;
    private String message;
    private List<String> scoring_list = null;
    private List<String> recommendation_list = null;
    public ApiOutput(int error) {
        this(error, "");
    }
    
    public ApiOutput(int errCode, String msg){
        this.returnCode = errCode;
        this.message = msg;
    }

//    public Object setData(Object data) {
//        try {
//            if (data != null) {
//                JSONObject ret = null;
//                try {
//
//                    if (data instanceof String) {
//                        ret = (JSONObject) new JSONParser().parse((String) data);
//                    } else {
//                        String dataString = new GsonBuilder().serializeNulls().create().toJson(data);
//                        ret = (JSONObject) new JSONParser().parse(dataString);
//                    }
//
//                } catch (ParseException e) {
//                }
//                if (ret != null) {
//                    data = ret;
//                }
//            }
//        } catch (Exception ex) {
//        }
//        return data;
//    }
    public void setScoringList(List<String> list){
        this.scoring_list = list;
    }
    
    public void setRecommadationList(List<String> list){
        this.recommendation_list = list;
    }

    public String toJString() {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        return gson.toJson(this);
        return new Gson().toJson(this);
    }
    
    public static enum STATUS_CODE{
        
        SUCCESS(200,"Success"),
        BAD_REQUEST(400,"Bad request"),
        SYSTEM_ERROR(500,"System error"),
        REQUEST_TIME_OUT(408,"Request timeout");
        
        public int errorCode;
        public String msg;
        
        private STATUS_CODE(int errorCode,String msg){
            this.errorCode = errorCode;
            this.msg = msg;
        }
    }
}
