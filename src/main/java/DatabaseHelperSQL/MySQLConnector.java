/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseHelperSQL;

import Constant.DBInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class MySQLConnector {

    private static final long serialVersionUID = 0L;

    private static final Logger LOGGER = Logger.getLogger(MySQLConnector.class);

    private final String hostName;
    private final String databaseName;
    private final int port;
    private final String userName;
    private final String password;
   
    private static Calendar calendar = new GregorianCalendar();
    
    private Connection cnn = null;

    public MySQLConnector() {
        this(DBInfo.hostName,
                DBInfo.databaseName,
                DBInfo.port,
                DBInfo.userName,
                DBInfo.password);
    }

    public MySQLConnector(String hostName, String databaseName, int port, String userName, String password) {
        this.hostName = hostName;
        this.databaseName = databaseName;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        String address = getAddress();    
        LOGGER.info("Create a connection: " + address);
        
        cnn = DriverManager.getConnection(address, this.userName, this.password);

        return cnn;
    }
    
    private String getAddress(){
        String address = "jdbc:mysql://"
                + hostName + ":"
                + port + "/"
                + databaseName;
        return address;
    }
    
    private boolean increasePoint(String gameID){
        return true;
    }
    
    public String createPointOfGameSQL(String gameID){
        String sql = "INSERT INTO "+DBInfo.pointOfGameTableString +"("+DBInfo.gameIDString+","+DBInfo.pointString+")"+ 
                    " VALUES(\'"+gameID+"\', 1)"+
                    " ON DUPLICATE KEY UPDATE "+DBInfo.pointString+" = "+DBInfo.pointString+" +1";
        return sql;
    }
    
    public String createTopScoreGameSQL(String session, String userID, String gameID){
        return this.createTopScoreGameSQL(session, userID, gameID, 0, 0);
    }
    
    public String createTopScoreGameSQL(String userID, String gameID, long amount){
        long currentWeek = this.getCurrentWeek();
        String sql = "UPDATE "+DBInfo.topScoreGameTableString+
                    " SET "+DBInfo.amountString+" = "+DBInfo.amountString+"+"+amount+","
                            +DBInfo.pointString+" = "+DBInfo.pointString+"* POWER("+2+","+DBInfo.weekString+"-"+currentWeek+")"+"+"+amount/10000+
                    " WHERE "+DBInfo.userIDString+"="+"\'"+userID+"\'"+
                    " AND "+DBInfo.gameIDString+"="+"\'"+gameID+"\'";
        return sql;
    }
            
    public String createTopScoreGameSQL(String session, String userID, String gameID, long point, long amount){
        long currentWeek = this.getCurrentWeek();
        
        String sql = "INSERT INTO "+DBInfo.topScoreGameTableString+"("+DBInfo.sessionString+","
                                                                    +DBInfo.userIDString+","
                                                                    +DBInfo.gameIDString+","
                                                                    +DBInfo.pointString+","
                                                                    +DBInfo.amountString+","
                                                                    +DBInfo.weekString+","
                                                                    +DBInfo.loginTimesString+")"
                    +" VALUES(\'"+session+"\',"+"\'"+userID+"\',"+"\'"+gameID+"\',"+point+","+amount+","+currentWeek+", 1)"
                    +" ON DUPLICATE KEY UPDATE "+DBInfo.pointString+" = "+DBInfo.pointString +"+ "+ point+","+
                                                DBInfo.loginTimesString+" = "+DBInfo.loginTimesString+"+ 1,"+
                                                DBInfo.weekString+" = "+currentWeek;
        return sql;
    }
    private long getCurrentWeek(){
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        MySQLConnector connector = new MySQLConnector();
        Connection cnn = connector.getConnection();
        
        Statement statement = cnn.createStatement();
        //LOGGER.info("Create a connection: " + cnn.createTopScoreGameSQL("152hdksfv261290","273823","sahjas",20,20000));
        //String sql1 = connector.createPointOfGameSQL("123");
        //String sql2 = connector.createTopScoreGameSQL("123", "123", 100000);
        String sql3 = connector.createTopScoreGameSQL("726372", "123", "123");
        String str = String.format("UPDATE %s SET %s = %s + %d, %s = %s * POWER(2,%s-%d)+%d WHERE %s = \'%s\' AND %s = \'%s\'",
                                    DBInfo.topScoreGameTableString,
                                    DBInfo.amountString,
                                    DBInfo.amountString,
                                    10000,
                                    DBInfo.pointString,
                                    DBInfo.pointString,
                                    DBInfo.weekString,
                                    50,
                                    1,
                                    DBInfo.sessionString,
                                    "123",
                                    DBInfo.gameIDString,
                                    "123");
        LOGGER.info(str);
//        LOGGER.info("Create a SQL: " + sql1);
//        LOGGER.info("Create a SQL: " + sql2);
       
        //statement.executeUpdate(sql1);
//        int row = statement.executeUpdate(sql3);
//        LOGGER.info("Effect rows: "+row);
        //String sql = "INSERT INTO `RecommentList`(`GameID`,`Point`) "
        //        + "VALUES('152hdksfv261290',1);";
        //int rowCount = statement.executeUpdate(sql);
        //System.out.println(rowCount);
    }
}
