/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connection;


import exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class DatabaseMySQLConnection implements DatabaseConnection {

    private static final long serialVersionUID = 0L;

    private static final Logger LOGGER = Logger.getLogger(DatabaseMySQLConnection.class);

    private DatabaseInfo databaseInfo = null;
    private static Calendar calendar = new GregorianCalendar();
    private Connection connection = null;

    public DatabaseMySQLConnection(DatabaseInfo databaseInfo) throws SQLException {
        this.databaseInfo = databaseInfo;
    }
   
    @Override
    public void createConnection() throws DatabaseException {
        if(this.connection != null){
            return;
        }
        String URLPattern = "jdbc:mysql://%s:%d/%s?useSSL=true";
        
        String serverName = databaseInfo.getHostAddress();
        int port = databaseInfo.getPort();
        String databaseName = databaseInfo.getDatabaseName();
        String userName = databaseInfo.getUserName();
        String password = databaseInfo.getPassword();

        String address = String.format(URLPattern, serverName, port, databaseName);

        try {
            this.connection = DriverManager.getConnection(address, userName, password);
        } catch (SQLException ex) {
            LOGGER.error("Create connection failure with SQL instrustment: " + address + ", user:" + userName + " password:" + password + "\n");
            throw new DatabaseException("Create connection failure with SQL instrustment: " + address + ", user:" + userName + " password:" + password + "\n");
        }
    }
    @Override
    public void close() throws DatabaseException {
        if(this.connection != null){
            try {
                this.connection.close();
            } catch (SQLException ex) {
                throw new DatabaseException();
            }
        }
    }
    
    public DatabaseInfo getDatabaseInfo() {
        return this.databaseInfo;
    }

    public Connection getConnection() throws SQLException {
        return this.connection;
    }

    @Override
    public boolean ping(){
        try {
            return !this.connection.isClosed();
        } catch (SQLException ex) {
            return false;
        }
    }
    
}
