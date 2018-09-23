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
public class GenerateRequestFromLogin extends GenerateRequest {

    private static final long serialVersionUID = 0L;
    private final static String bufferToRandom = "1234567890abcdefghijklmnopqrstuvxyzwABCDEFGHIJKLMNOPQRSTUVXYZW";
    
    private static int lenOfSessionString = 10;
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
       
        String session = generateSession();
        String gameID = generateGameID();
        String userID = generateUserID();
        
        return parse2JSON(session,gameID,userID).toJSONString();
    }
    
    public static void setLenOfSessionString(int len){
        lenOfSessionString = len;
    }
    
    
    public static String generateSession() {
        Random random = new Random();
        StringBuilder session = new StringBuilder(lenOfSessionString);
        for(int i=0;i<lenOfSessionString;i++){
            int index = random.nextInt(lenOfBuffer);
            session.append(bufferToRandom.charAt(index));
        }
        return session.toString();
    }
    
    private static JSONObject parse2JSON(String session, Game game, String userID){
        JSONObject objResult = new JSONObject();
        JSONObject objGame = new JSONObject();
        
        objResult.put("session", session);
        
        objGame.put("name", game.getName());
        objGame.put("typegame", game.getTypeGame());
        objResult.put("game", objGame);
        
        objResult.put("userID",userID);
        
        return objResult;
    }
    private static JSONObject parse2JSON(String session, String gameID, String userID){
        JSONObject objResult = new JSONObject();
        JSONObject objGame = new JSONObject();
        
        objResult.put("session", session);
        objResult.put("gameID", gameID);
        objResult.put("userID",userID);
        
        return objResult;
    }
    public static void main(String[] args) {
        System.out.println(GenerateRequestFromLogin.get());
    }
}
