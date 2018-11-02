package strategy.database.mapping.missing;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package Strategy.DatabaseMappingMissing;
//
//import Constant.DBConstantString;
//import Constant.PoolConstantString;
//import DatabaseConnectionPool.DatabaseConnectionPoolLevelDB;
//import DatabaseConnectionPool.DatabaseLevelDBConnection;
//import DatabaseConnectionPool.DatabaseLevelDBConnectionFactory;
//import Exception.DatabaseException;
//import Log.LogPayment;
//import com.google.gson.JsonObject;
//
//import org.apache.log4j.Logger;
//
//import org.iq80.leveldb.*;
//import static org.fusesource.leveldbjni.JniDBFactory.*;
//import java.io.*;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Properties;
//import java.util.logging.Level;
//import java.util.prefs.Preferences;
//import org.apache.commons.net.ntp.NTPUDPClient;
//import org.apache.commons.net.ntp.TimeInfo;
//import org.ini4j.Ini;
//import org.ini4j.IniPreferences;
///**
// *
// * @author root
// */
//public class DatabaseMappingMissingLevelDB implements DatabaseMappingMissingStrategy{
//    
//    private static final Logger LOGGER = Logger.getLogger(DatabaseMappingMissingLevelDB.class);
//    
//    private static DatabaseConnectionPoolLevelDB pool = null;
//    
//    private Options option;
//    private String databaseMissingName;
//    private DB db;
//    
//    static{
//        try {
//            Ini ini = new Ini(new File(Constant.PathConstant.PATH_TO_DATABASE_CONFIG_FILE));
//            Preferences pref = new IniPreferences(ini).node(Constant.PathConstantString;
//            
//            String databaseName = 
//            this.config.put(PoolConstantString.BLOCK_WHEN_EXHAUSTED, pref.getBoolean(PoolConstantString.BLOCK_WHEN_EXHAUSTED, true));
//            DatabaseLevelDBConnectionFactory factory = new DatabaseLevelDBConnectionFactory()
//                    } catch (IOException ex) {
//            java.util.logging.Logger.getLogger(DatabaseMappingMissingLevelDB.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
//    public DatabaseMappingMissingLevelDB(String databaseMissingName){
//        this(databaseMissingName,new Options().createIfMissing(true));
//    }
//    public DatabaseMappingMissingLevelDB(String databaseMissingName, Options option){
//        this.option = option;
//        this.databaseMissingName = databaseMissingName;
//    }
//    
//    @Override
//    public void writeLogToDatabase(LogPayment logPayment) throws DatabaseException,IOException{
//        db = factory.open(new File(this.databaseMissingName), option);
//        try {
//            Date currentDate = this.getDate();
//            DateFormat dateFormat = new SimpleDateFormat(Configuration.ConfigOfSystem.formatDate);
//            
//            String dateString = dateFormat.format(this.getDate().getTime());
//            long timeStamp = currentDate.getTime();
//
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty(DBConstantString.DATE_STRING, dateString);
//            jsonObject.addProperty(DBConstantString.USERID_STRING, logPayment.getUserID());
//            jsonObject.addProperty(DBConstantString.GAMEID_STRING, logPayment.getGameID());
//            jsonObject.addProperty(DBConstantString.AMOUNT_STRING, logPayment.getAmount());
//
//            String value = jsonObject.toString();
//
//            db.put(bytes(String.valueOf(timeStamp)), bytes(value));
//        } finally {
//            db.close();
//        }
//        //LOGGER.info("Mapping misssed with" +value);
//    }
//    
//    private Date getDate() throws UnknownHostException, IOException{
//        NTPUDPClient timeClient = new NTPUDPClient();
//        
//        InetAddress inetAddress = InetAddress.getByName(Constant.PathConstant.TIME_SERVER_ADDRESS);
//        TimeInfo timeInfo = timeClient.getTime(inetAddress);
//        long returnTime = timeInfo.getReturnTime();
//        
//        Date time = new Date(returnTime);
//        long systemtime = System.currentTimeMillis();
//        
//        timeInfo.computeDetails();
//        
//        Date realdate = new Date(systemtime + timeInfo.getOffset());
//        
//        return realdate;
//    }
//    
//    
////    public static void main(String[] args) throws Exception {
////        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
////        NTPUDPClient timeClient = new NTPUDPClient();
////        InetAddress inetAddress = InetAddress.getByName(Constant.PathConstantString.TIME_SERVER_ADDRESS);
////        TimeInfo timeInfo = timeClient.getTime(inetAddress);
////        long returnTime = timeInfo.getReturnTime();
////        Date time = new Date(returnTime);
////        long systemtime = System.currentTimeMillis();
////        timeInfo.computeDetails();
////        Date realdate = new Date(systemtime + timeInfo.getOffset());
////        System.out.println("Time from " + Constant.PathConstantString.TIME_SERVER_ADDRESS + ": " + time);
////        System.out.println("Time from " + Constant.PathConstantString.TIME_SERVER_ADDRESS + ": " + realdate);
////        System.out.println(""+time.getTime());
////    }
//}
