/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

/**
 *
 * @author root
 */
public class DatabaseInfo {
    private static final long serialVersionUID = 8475847855284768456L;
    
    private String hostAddress;
    private int port;
    private String databaseName;
    private String userName;
    private String password;
    
    private static String DEFAULT_HOST = "localhost";
    private static String DEFAULT_USER_NAME= "vng.default.user";
    private static String DEFAULT_DATABASE_NAME = "vng.default.name";
    private static int DEFAULT_PORT = 666;

    public DatabaseInfo(String databaseName){
        this(DEFAULT_HOST,DEFAULT_PORT,databaseName);
    }
    
    public DatabaseInfo(String host,int port){
        this(host,port,DEFAULT_DATABASE_NAME);
    }
    
    public DatabaseInfo(String hostAddress,int port, String databaseName){
        this(hostAddress,
                port,
                databaseName,
                DEFAULT_USER_NAME,
                null);
    }
    public DatabaseInfo(String hostAddress, int port, String databaseName, String userName, String password) {
        this.hostAddress = hostAddress;
        this.port = port;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }  
    
}
