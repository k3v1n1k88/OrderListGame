/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import DatabaseHelperSQL.DatabaseInfo;
import DatabaseHelperSQL.DatabaseController;
import Configuration.ConfigOfSystem;
import Constant.DBInfo;
import Exception.DatabaseException;
import Exception.ParseLogException;
import Object.LogLogin;
import Object.LogPayment;
import VNG.generate.GenerateRequestFromLogin;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

/**
 *
 * @author root     
 */
public class DatabasePerformSQL implements DatabasePerformStrategy{
    
    private static final Logger LOGGER = Logger.getLogger(DatabasePerformSQL.class);
    private final DatabaseController databaseController; 
    private static Calendar calendar = new GregorianCalendar();
    private DatabaseMappingMissingStrategy databaseMappingMissingStrategy;

    

    public DatabasePerformSQL(){
        this(new DatabaseMappingMissingLevelDB());
    }
    
    public DatabasePerformSQL(DatabaseMappingMissingStrategy databaseMappingMissingStrategy){
        this(DBInfo.hostName,
            DBInfo.port,
            DBInfo.databaseName,
            DBInfo.userName,
            DBInfo.password,
            databaseMappingMissingStrategy);
    }
    
    public DatabasePerformSQL(DatabaseInfo databaseInfo){
        this(databaseInfo.getServerName(),
            databaseInfo.getPort(),
            databaseInfo.getDatabaseName(),
            databaseInfo.getUserName(),
            databaseInfo.getPassword(),
            new DatabaseMappingMissingLevelDB());           
    }
    public DatabasePerformSQL(String hostName, int port, String databaseName, String userName, String password) throws IOException{
        this(hostName,port,databaseName,userName,password,new DatabaseMappingMissingLevelDB());
    }
    public DatabasePerformSQL(String hostName, int port, String databaseName, String userName, String password,DatabaseMappingMissingStrategy databaseMappingMissingStrategy){
        this.databaseController = new DatabaseController(hostName,port,databaseName,userName,password);
        this.databaseMappingMissingStrategy = databaseMappingMissingStrategy;
    }
    

    public void setDatabaseMappintMissingStrategy(DatabaseMappingMissingStrategy databaseMappingMissingStrategy){
        this.databaseMappingMissingStrategy = databaseMappingMissingStrategy;
    }
    
    @Override
    public void accessToDatabase(LogLogin logLogin) throws SQLException{
        Connection cnn = databaseController.getConnection();
        String sql="";
        try{
            String format = "INSERT INTO %s(%s,%s,%s,%s,%s,%s)"+
                            " VALUES(\'%s\',\'%s\',\'%s\',%d,%d,%d)"+
                            " ON DUPLICATE KEY UPDATE"+
                            " %s = %s*(%s-%d) + %d,"+
                            " %s = %d,"+
                            " %s = %s + %d";
            long week = getWeek();
            Statement statement = cnn.createStatement();
            sql = String.format(format, DBInfo.topScoreGameTableString,
                                                DBInfo.sessionString,
                                                DBInfo.userIDString,
                                                DBInfo.gameIDString,
                                                DBInfo.pointString,
                                                DBInfo.weekString,
                                                DBInfo.loginTimesString,
                                                logLogin.getSession(),
                                                logLogin.getUserID(),
                                                logLogin.getGameID(),
                                                ConfigOfSystem.pointPerLogin,
                                                week,
                                                1,
                                                DBInfo.pointString,
                                                DBInfo.pointString,
                                                DBInfo.weekString,
                                                week,
                                                ConfigOfSystem.pointPerLogin,
                                                DBInfo.weekString,
                                                week,
                                                DBInfo.loginTimesString,
                                                DBInfo.loginTimesString,
                                                1);
            //LOGGER.info("SQL instructment: "+sql+"\n");
            int res = statement.executeUpdate(sql);
 
            //return res;
        } catch (SQLException ex) {
            LOGGER.error("Error with SQL statement: "+sql+".\nDetail:"+ex);
            throw ex;
        }finally{
            cnn.close();
        }
    }
    
    private int updatePointOfGame(LogLogin logLogin) throws SQLException{
        Connection cnn = databaseController.getConnection();
        String sql="";
        try{
            String format = "UPDATE %s,%s"+
                            " SET %s.%s = %s.%s + 1"+
                            " WHERE %s.%s = %s.%s ";
            Statement statement = cnn.createStatement();
            sql = String.format(format, DBInfo.pointOfGameTableString,
                                        DBInfo.topScoreGameTableString,
                                        DBInfo.pointOfGameTableString,
                                        DBInfo.pointString,
                                        DBInfo.pointOfGameTableString,
                                        DBInfo.pointString,
                                        DBInfo.pointOfGameTableString,
                                        DBInfo.gameIDString,
                                        DBInfo.topScoreGameTableString,
                                        DBInfo.gameIDString);
            //LOGGER.info("SQL instructment: "+sql+"\n");
            int res = statement.executeUpdate(sql);
            return res;
        } catch (SQLException ex) {
            LOGGER.error("Error with SQL statement: "+sql+".\nDetail:"+ex);
            throw ex;
        }finally{
            cnn.close();
        }
    }

    @Override
    public void mappingToDatabase(LogPayment logPayment) throws DatabaseException, SQLException, IOException {
        Connection cnn = databaseController.getConnection();
        String sql="";
        try{
            long bonusPoint = logPayment.getAmount()/10000;
            String format = "UPDATE %s"+
                            " SET %s = %s + %d"+
                            " %s = %s + %d"+
                            " WHERE %s = %s ";
            Statement statement = cnn.createStatement();
            sql = String.format(format, DBInfo.topScoreGameTableString,
                                        DBInfo.pointString,
                                        DBInfo.pointString,
                                        bonusPoint,
                                        DBInfo.amountString,
                                        DBInfo.amountString,
                                        logPayment.getAmount(),
                                        DBInfo.userIDString,
                                        DBInfo.userIDString);
            //LOGGER.info("SQL instructment: "+sql+"\n");
            int res = statement.executeUpdate(sql);
            if(res <= 0 ){
                databaseMappingMissingStrategy.writeLogToDatabase(logPayment);
            }
        } catch (SQLException ex) {
            LOGGER.error("Error with SQL statement: "+sql+".\nDetail:"+ex);
            throw ex;
        }finally{
            cnn.close();
        }
    }

    private long getWeek(){
        long week = calendar.get(Calendar.WEEK_OF_YEAR)+ calendar.get(Calendar.YEAR);
        return week;
    }
    
    private int initGameOfPoint(LogLogin logLogin) throws SQLException{
        Connection cnn = databaseController.getConnection();
        String sql = "";
        try {
            Statement statement = cnn.createStatement();
            String format = "INSERT IGNORE INTO %s(%s)"+
                            " VALUES(\'%s\')";
            sql = String.format(format, DBInfo.pointOfGameTableString,
                                                    DBInfo.gameIDString,
                                                    logLogin.getGameID());
            //LOGGER.info("SQL instrusment:"+sql);
            int affectRows = statement.executeUpdate(sql);
            return affectRows;
        } catch(SQLException ex){
            LOGGER.error("Error with SQL statement: "+sql+".\nDetail:"+ex);
            throw ex;
        }finally {
            cnn.close();
        }
    }

    @Override
    public String getList(String session) throws DatabaseException, SQLException{
        JsonArray orderListGame = this.getListTopGame(session);
        JsonArray recommentListGame = this.getListRecommentGame();
        
//        JsonObject jsonObjOrderListGame = new JsonObject();
//        jsonObjOrderListGame.add(DBInfo.recommentListGame, recommentListGame);
//        
//        JsonObject jsonObjRecommentListGame = new JsonObject();
//        jsonObjRecommentListGame.add(DBInfo.orderListGame, orderListGame);
        
        JsonObject res = new JsonObject();
        res.add(DBInfo.recommentListGame, recommentListGame);
        res.add(DBInfo.orderListGame, orderListGame);
        return res.toString();
    }
    private JsonArray getListTopGame(String session) throws SQLException{
        Connection cnn = databaseController.getConnection();
        String sql;
        JsonArray jsonArray = new JsonArray();
        try{
            String format = "SELECT %s"
                           +" FROM %s"
                           +" WHERE %s = \'%s\' "
                           +" ORDER BY %s DESC";
            Statement statement = cnn.createStatement();
            sql = String.format(format, DBInfo.gameIDString,
                                        DBInfo.topScoreGameTableString,
                                        DBInfo.sessionString,
                                        session,
                                        DBInfo.gameIDString);
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                JsonPrimitive element = new JsonPrimitive(rs.getString(DBInfo.gameIDString));
                jsonArray.add(element);
            }
            LOGGER.info(sql);
            return jsonArray;
        }finally{
            cnn.close();
        }
    }
    private JsonArray getListRecommentGame() throws SQLException{
        Connection cnn = databaseController.getConnection();
        String sql;
        JsonArray jsonArray = new JsonArray();
        try{
            String format = "SELECT %s"
                           +" FROM %s"
                           +" ORDER BY %s DESC"
                           +" LIMIT %s";
            sql = String.format(format, DBInfo.gameIDString,
                                        DBInfo.pointOfGameTableString,
                                        DBInfo.gameIDString,
                                        DBInfo.numsRecommentGame);
            Statement statement = cnn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                JsonPrimitive element = new JsonPrimitive(rs.getString(DBInfo.gameIDString));
                jsonArray.add(element);
            }
            
            LOGGER.info(sql);
            return jsonArray;
        }finally{
            cnn.close();
        }
    }
    public static void main(String[] args) throws ParseLogException, SQLException, ParseException, DatabaseException, IOException {
        DatabasePerformSQL databasePerformSQL = new DatabasePerformSQL();
        LogLogin logLogin = LogLogin.logToObj(GenerateRequestFromLogin.get());
        String res = databasePerformSQL.getList("2");
        System.out.println(res);
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(res, JsonElement.class);
        JsonObject obj = element.getAsJsonObject();
        System.out.println(obj);
    }
}
