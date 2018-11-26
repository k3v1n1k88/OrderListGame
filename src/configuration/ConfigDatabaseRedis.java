/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import exception.ConfigException;
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
        super(pathFile,constant.DBConstantString.DATABASE_REDIS);
        
        this.host = this.prefs.get(constant.DBConstantString.HOST, DEFAULT_HOST);
        this.port = this.prefs.getInt(constant.DBConstantString.PORT, DEFAULT_PORT);
        this.connectionTimeoutMillius = this.prefs.getInt(constant.DBConstantString.CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT_MILLIUS);
        this.soTimeoutMillius = this.prefs.getInt(constant.DBConstantString.SOCKET_TIMEOUT, DEFAULT_SO_TIMEOUT_MILLIUS);
        this.password = this.prefs.get(constant.DBConstantString.PASSWORD, null);
        this.ssl = this.prefs.getBoolean(constant.DBConstantString.SSL, DEFAULT_SSL);
        
        LOGGER.info("Configuration of database Redis:"
                    +"\n"+constant.DBConstantString.HOST+ " : "+ this.host
                    +"\n"+constant.DBConstantString.PORT+ " : "+ this.port
                    +"\n"+constant.DBConstantString.PASSWORD+ " : "+ this.password
                    +"\n"+constant.DBConstantString.CONNECTION_TIMEOUT+ " : "+ this.connectionTimeoutMillius
                    +"\n"+constant.DBConstantString.SOCKET_TIMEOUT+ " : "+ this.soTimeoutMillius
        );
    }
    
    public ConfigDatabaseRedis() throws ConfigException{
        this(constant.PathConstant.PATH_TO_DATABASE_REDIS_CONFIG_FILE);
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
//    private static String DEFAULT_PASSWORD = "vng.com.vn";
    private static boolean DEFAULT_SSL = false;
    private static int DEFAULT_CONNECTION_TIMEOUT_MILLIUS = 2000;
    private static int DEFAULT_SO_TIMEOUT_MILLIUS = 2000;
    
}
