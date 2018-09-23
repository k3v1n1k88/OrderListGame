/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Configuration.ConfigOfSystem;
import Constant.DBInfo;
import Object.LogLogin;
import Object.LogPayment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sun.istack.internal.logging.Logger;
import org.iq80.leveldb.*;
import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeSet;
import org.iq80.leveldb.Options;

/**
 *
 * @author root
 */
public class DatabasePerformLevelDB implements DatabasePerformStrategy{
    
    private static final Logger LOGGER = Logger.getLogger(DatabasePerformLevelDB.class);
    private static Calendar calendar = new GregorianCalendar();
    
    private Options options;
    private String databaseName;
    private DatabaseMappingMissingStrategy databaseMappingMissingStrategy;
    public DB dbSession;
    private DB dbMapping;
    
    private String databaseRecommentName = DBInfo.databaseRecommentName;
    DatabaseRecommentStrategy databaseRecommentLStrategy = new DatabaseRecommentHashTable();
    
    public DatabasePerformLevelDB() throws IOException{
        this(DBInfo.databaseName);
    }
    public DatabasePerformLevelDB(String databaseName) throws IOException{
        this(databaseName,new Options().createIfMissing(true),new DatabaseMappingMissingLevelDB());
    }
    
    public DatabasePerformLevelDB(String databaseName,Options option,DatabaseMappingMissingStrategy databaseMappingMissingStrategy){
        this.databaseName = databaseName;
        this.options = option;
        this.databaseMappingMissingStrategy = databaseMappingMissingStrategy;
    }
    
    @Override
    public void accessToDatabase(LogLogin logLogin) throws Exception {
        this.dbSession = factory.open(new File(databaseName), options);
        ArrayList<String> listGameNeedAddPoint = new ArrayList();
        try {
            //Get from database 
            String infoSession = asString(this.dbSession.get(bytes(logLogin.getSession())));

            //Session is not exist
            if (infoSession == null) {
                //Create new array json for this session
                JsonArray jsonArr = new JsonArray();

                //Create new elements
                JsonObject jsonObj = this.createNewElement(logLogin.getUserID(), logLogin.getGameID());

                jsonArr.add(jsonObj);

                this.dbSession.put(bytes(logLogin.getSession()), bytes(jsonArr.toString()));

                //update database mapping
                dbMapping = factory.open(new File(DBInfo.databaseMappingName), options);
                try{
                    this.dbMapping.put(bytes(logLogin.getUserID()), bytes(logLogin.getSession()));
                }finally{
                    this.dbMapping.close();
                }
            } //Session is exist, we update point and week
            else {
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonArr = jsonParser.parse(infoSession).getAsJsonArray();
                JsonObject jsonObj = null;
                JsonArray jsonArrNew = new JsonArray();
                for (int i = 0; i < jsonArr.size(); i++) {
                    jsonObj = (JsonObject) jsonArr.get(i);
                    String gameID = jsonObj.get(DBInfo.gameIDString).getAsString();
                    listGameNeedAddPoint.add(gameID);
                    if (gameID.equals(logLogin.getGameID())) {
                        long point = jsonObj.get(DBInfo.pointString).getAsLong();
                        long amount = jsonObj.get(DBInfo.amountString).getAsLong();
                        long week = jsonObj.get(DBInfo.weekString).getAsLong();

                        jsonObj.remove(DBInfo.pointString);
                        jsonObj.remove(DBInfo.amountString);
                        jsonObj.remove(DBInfo.weekString);

                        point += (long) point * Math.pow(2.0, week - this.getWeek());
                        amount += 0;
                        week = this.getWeek();

                        jsonObj.addProperty(DBInfo.pointString, point);
                        jsonObj.addProperty(DBInfo.amountString, amount);
                        jsonObj.addProperty(DBInfo.weekString, week);
                    }
                    jsonArrNew.add(jsonObj);
                }

                //Game is not exist in database session
                if (jsonObj == null) {
                    //Create new elements
                    JsonObject objNew = this.createNewElement(logLogin.getUserID(), logLogin.getGameID());
                    databaseRecommentLStrategy = new DatabaseRecommentHashTable();
                    databaseRecommentLStrategy.update(listGameNeedAddPoint);
                    jsonArrNew.add(objNew);
                }
                this.sortAndPushDatabaseAgain(logLogin.getSession(), jsonArrNew.toString());
            }
        } finally {
            this.dbSession.close();
        }
    }

    @Override
    public void mappingToDatabase(LogPayment logPayment) throws Exception {
        dbMapping = factory.open(new File(DBInfo.databaseMappingName), options);
        try {
            String session = asString(this.dbMapping.get(bytes(logPayment.getUserID())));

            //Missing mapping
            if (session == null) {
                this.databaseMappingMissingStrategy.writeLogToDatabase(logPayment);
            } //if mapping, we have 2 scenario: gameID is and is not exist in this session
            else {
                String infoSession = asString(this.dbMapping.get(bytes(session)));
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonArr = jsonParser.parse(infoSession).getAsJsonArray();
                JsonObject jsonObj = null;
                JsonArray jsonArrNew = new JsonArray();
                for (int i = 0; i < jsonArr.size(); i++) {
                    jsonObj = (JsonObject) jsonArr.get(i);

                    //if gameID is exist, we update point and current week
                    if (jsonObj.get(DBInfo.gameIDString).getAsString().equals(logPayment.getGameID())) {
                        
                        long point = jsonObj.get(DBInfo.pointString).getAsLong();
                        long amount = jsonObj.get(DBInfo.amountString).getAsLong();
                        long week = jsonObj.get(DBInfo.weekString).getAsLong();

                        jsonObj.remove(DBInfo.pointString);
                        jsonObj.remove(DBInfo.amountString);
                        jsonObj.remove(DBInfo.weekString);

                        point += (long) point * Math.pow(2.0, week - this.getWeek());
                        week = this.getWeek();
                        amount += logPayment.getAmount();

                        jsonObj.addProperty(DBInfo.pointString, point);
                        jsonObj.addProperty(DBInfo.amountString, amount);
                        jsonObj.addProperty(DBInfo.weekString, week);
                    }

                    jsonArrNew.add(jsonObj);
                }
                //if gameId is not exist,we create new elements to push into this session
                if (jsonObj == null) {
                    JsonObject jsonObjNew = this.createNewElement(logPayment.getUserID(), logPayment.getGameID());
                    jsonArrNew.add(jsonObjNew);
                }

                this.sortAndPushDatabaseAgain(session, jsonArrNew.toString());
            }
        } finally {
            this.dbMapping.close();
        }
        
    }

    @Override
    public String getList(String session) throws IOException {
        JsonArray orderListGame = this.getListTopGame(session);
        JsonArray recommentListGame = this.getListRecommentGame();
        JsonObject res = new JsonObject();
        res.add(DBInfo.recommentListGame, recommentListGame);
        res.add(DBInfo.orderListGame, orderListGame);
        return res.toString();
    }
    
    private JsonArray getListTopGame(String session) throws IOException{
        this.dbSession = factory.open(new File(DBInfo.databaseName), options);
        try{
            String infoSession = asString(this.dbSession.get(bytes(session)));
            JsonParser jsonParser = new JsonParser();
            JsonArray jsonArr = jsonParser.parse(infoSession).getAsJsonArray();
            JsonObject jsonObj = null;
            JsonArray jsonArrListGameID = new JsonArray();
            for (int i = 0; i < jsonArr.size(); i++){
                jsonObj = (JsonObject) jsonArr.get(i);
                JsonPrimitive element = new JsonPrimitive(jsonObj.get(DBInfo.gameIDString).getAsString());
                jsonArrListGameID.add(jsonObj);
            }
            return jsonArrListGameID;
        }finally{
            this.dbSession.close();
        }
    }
    
    private JsonArray getListRecommentGame(){
        return this.databaseRecommentLStrategy.getTop(ConfigOfSystem.maxRecommentGame);
    }
    public static void main(String[] args) throws IOException {
//        DatabasePerformLevelDB databasePerformLevelDB = new DatabasePerformLevelDB();
//        //System.out.println(databasePerformLevelDB.dbSession.get(bytes("123"))==null);
//        String input = "[\n" +
//"  {\n" +
//"    \"Point\": 1,\n" +
//"    \"LoginTime\": 100,\n" +
//"    \"Week\": 100,\n" +
//"    \"userID\": \"1\",\n" +
//"    \"Amount\": 50000,\n" +
//"    \"GameID\": \"1\"\n" +
//"  },\n" +
//"  {\n" +
//"    \"Point\": 4,\n" +
//"    \"LoginTime\": 100,\n" +
//"    \"Week\": 100,\n" +
//"    \"userID\": \"2\",\n" +
//"    \"Amount\": 50000,\n" +
//"    \"GameID\": \"2\"\n" +
//"  },\n" +
//"    {\n" +
//"    \"Point\": 3,\n" +
//"    \"LoginTime\": 100,\n" +
//"    \"Week\": 100,\n" +
//"    \"userID\": \"2\",\n" +
//"    \"Amount\": 50000,\n" +
//"    \"GameID\": \"3\"\n" +
//"  }\n" +
//"]";

    }
    private long getWeek(){
        long week = calendar.get(Calendar.WEEK_OF_YEAR)+ calendar.get(Calendar.YEAR);
        return week;
    }
    
    private void sortAndPushDatabaseAgain(String session, String infoSession) {
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArr = jsonParser.parse(infoSession).getAsJsonArray();
        
        TreeSet<JsonObject> jsonArrayAsTreeSet = new TreeSet<>((Object o1, Object o2) -> {
            JsonObject obj1 = (JsonObject) o1;
            JsonObject obj2 = (JsonObject) o2;
            long value1 = Long.parseLong(obj1.get("Point").getAsString());
            long value2 = Long.parseLong(obj2.get("Point").getAsString());
            return value1 > value2 ? -1 : value1 == value2 ? 0 : 1;
        });

        for (int j = 0; j < jsonArr.size(); j++) {
            JsonObject obj = (JsonObject) jsonArr.get(j);
            System.out.println(obj.toString());
            jsonArrayAsTreeSet.add(obj);
        }

        Iterator<JsonObject> itr = jsonArrayAsTreeSet.iterator();
        JsonArray jsonArrNew = new JsonArray();

        while (itr.hasNext()) {
            jsonArr.add(itr.next());
        }

        this.dbSession.put(bytes(session), bytes(jsonArrNew.toString()));

        jsonArrayAsTreeSet.clear();
    }
    
    private JsonObject createNewElement(String userID,String gameID){
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty(DBInfo.pointString, ConfigOfSystem.pointPerLogin);
        jsonObj.addProperty(DBInfo.userIDString, userID);
        jsonObj.addProperty(DBInfo.gameIDString, gameID);
        jsonObj.addProperty(DBInfo.amountString, ConfigOfSystem.initAmount);
        jsonObj.addProperty(DBInfo.loginTimesString, ConfigOfSystem.initLoginTimes);
        jsonObj.addProperty(DBInfo.weekString, this.getWeek());
        return jsonObj;
    }
}
