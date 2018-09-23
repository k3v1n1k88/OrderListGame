/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseHelperSQL;

import DatabaseHelperSQL.DatabaseConnector;
import Constant.DBInfo;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author root
 */
public class DatabaseController {
    private static DatabaseController instance = null;
    private DatabaseInfo databaseInfo = null;
    
    public DatabaseController(String hostName, int port, String databaseName, String userName, String password){
        databaseInfo = new DatabaseInfo(hostName,
                                        port,
                                        databaseName,
                                        userName,
                                        password);
    }
    
    public Connection getConnection() throws SQLException{
        return DatabaseConnector.Factory.getConnection(databaseInfo);
    }
    
    public boolean checkConnection(Connection connnection){
        return true;
    }
    public static void main(String[] args) throws SQLException {
        DatabaseController databaseController = new DatabaseController(null,0,null,null,null);
        Connection cnn = databaseController.getConnection();
    }
}
