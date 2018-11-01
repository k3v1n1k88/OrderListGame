/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VNG.generate;

import java.util.Random;
import org.json.simple.JSONObject;

/**
 *
 * @author root
 */
public class GenerateRequestFromPayment extends GenerateRequest{
    private static final long serialVersionUID = 0L;
    private String session;
    private String userID;
    private JSONObject game;
    private String nameOfGame;
    private String typeOfGame;

    private static final int lenOfSessionString = 10;
    private static final int lenOfUserIDString = 10;

    private final static String bufferToRandom = "1234567890abcdefghijklmnopqrstuvxyzwABCDEFGHIJKLMNOPQRSTUVXYZW";
    private static final int lenOfBuffer = 26;
    /**
     * Create Object JSON have 3 properties: {session,game,userID} Example:
     * "myObj":[ { "session":"12XHKGw", "game": { "nameOfGame":"Gunny",
     * "typeOfGame":"Mobile", }, "userID":"01234512" } ]
     *
     * @return JSONObject
     */
    public static String get() {
        JSONObject obj = new JSONObject();
        
        long amount = generateAmount();
        String game = GenerateRequest.generateGameID();
        String userID = generateUserID();
        
        return parse2JSON(amount,game,userID).toJSONString();
    }
    
    private static long generateAmount(){
        Random rand = new Random();
        return (rand.nextInt(10000)+1)*10000;
    } 
   
    private static JSONObject parse2JSON(long amount, String gameID, String userID){
        JSONObject objResult = new JSONObject();
        JSONObject objGame = new JSONObject();
        
        objResult.put("amount", amount);
        objResult.put("gameID", gameID);
        objResult.put("userID",userID);
        
        return objResult;
    }
    
    public static void main(String[] args) {
        System.out.println(GenerateRequestFromPayment.get());
    }
}
