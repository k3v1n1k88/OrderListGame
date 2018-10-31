/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuration;

import Exception.ConfigException;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ConfigDatabaseRedis extends ConfigurationAbstract {
    
    private static final Logger LOGGER = Logger.getLogger(ConfigDatabaseRedis.class);
    
    private String host ;
    private int port ;
    private String password ;
    private boolean ssl ;
    private int connectionTimeoutMillius ;
    private int soTimeoutMillius ;
    
    
    public ConfigDatabaseRedis(String pathFile) throws ConfigException {
        super(pathFile,Constant.DBConstantString.DATABASE_REDIS);
        
        this.host = this.prefs.get(Constant.DBConstantString.HOST, DEFAULT_HOST);
        this.port = this.prefs.getInt(Constant.DBConstantString.PORT, DEFAULT_PORT);
        this.connectionTimeoutMillius = this.prefs.getInt(Constant.DBConstantString.CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT_MILLIUS);
        this.soTimeoutMillius = this.prefs.getInt(Constant.DBConstantString.SOCKET_TIMEOUT, DEFAULT_SO_TIMEOUT_MILLIUS);
        this.password = this.prefs.get(Constant.DBConstantString.PASSWORD, DEFAULT_PASSWORD);
        this.ssl = this.prefs.getBoolean(Constant.DBConstantString.SSL, DEFAULT_SSL);
        
        System.out.println("Configuration of database Redis:"
                    +"\n"+Constant.DBConstantString.HOST+ " : "+ this.host
                    +"\n"+Constant.DBConstantString.PORT+ " : "+ this.port
                    +"\n"+Constant.DBConstantString.PASSWORD+ " : "+ this.password
                    +"\n"+Constant.DBConstantString.CONNECTION_TIMEOUT+ " : "+ this.connectionTimeoutMillius
                    +"\n"+Constant.DBConstantString.SOCKET_TIMEOUT+ " : "+ this.soTimeoutMillius
        );
    }
    
    public ConfigDatabaseRedis() throws ConfigException{
        this(Constant.PathConstant.PATH_TO_DATABASE_REDIS_CONFIG_FILE);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSSL() {
        return ssl;
    }

    public int getConnectionTimeoutMillius() {
        return connectionTimeoutMillius;
    }

    public int getSoTimeoutMillius() {
        return soTimeoutMillius;
    }    
    
    private static String DEFAULT_HOST = "localhost";
    private static int DEFAULT_PORT = 6379;
    private static String DEFAULT_PASSWORD = "vng.com.vn";
    private static boolean DEFAULT_SSL = false;
    private static int DEFAULT_CONNECTION_TIMEOUT_MILLIUS = 2000;
    private static int DEFAULT_SO_TIMEOUT_MILLIUS = 2000;
    
}
