/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseHelperSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class DatabaseConnector {
    public static final Logger LOGGER = Logger.getLogger(DatabaseConnector.class);
    public static final String URLPattern = "jdbc:mysql://%s:%d/%s?useSSL=true";
    
    private DatabaseInfo databaseInfo;
    
    public DatabaseConnector(DatabaseInfo databaseInfo){
        this.databaseInfo = databaseInfo;
    }
    public Connection getConnection() throws SQLException{
        String address = String.format(URLPattern, databaseInfo.getServerName(),databaseInfo.getPort(),databaseInfo.getDatabaseName());
        try {
            Connection cnn =  DriverManager.getConnection(address);
            //LOGGER.info("Create a connection to server. Info address: "+address);
            return cnn;
        } catch (SQLException ex) {
            throw ex;
        }
    }
    public static class Factory{  
        public static Connection getConnection(DatabaseInfo databaseInfo) throws SQLException{
            String serverName = databaseInfo.getServerName();
            int port = databaseInfo.getPort();
            String databaseName = databaseInfo.getDatabaseName();
            String userName = databaseInfo.getUserName();
            String password = databaseInfo.getPassword();
            
            String address = String.format(URLPattern, serverName,port,databaseName);
            
            Connection cnn = null;
            try {
                cnn = DriverManager.getConnection(address,userName,password);
                //LOGGER.info("Create a connection to server. Info address: "+address);
                return cnn;
            } catch (SQLException ex) {
                LOGGER.error("Create connection failure with SQL instrustment: "+address+", user:"+userName+" password:"+password+"\n");
                throw ex;
            }
        }
    }
}
