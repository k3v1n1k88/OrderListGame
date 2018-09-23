/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseHelperSQL;

/**
 *
 * @author root
 */
public class DatabaseInfo {
    private static final long serialVersionUID = 0L;
    
    private String serverName;
    private int port;
    private String databaseName;
    private String userName;
    private String password;

    public DatabaseInfo(String serverName, int port, String databaseName, String userName, String password) {
        this.serverName = serverName;
        this.port = port;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
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
