/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import constant.ScribeConstantString;
import exception.ConfigException;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;
import zcore.utilities.ScribeServiceClient;
/**
 *
 * @author root
 */
public class ConfigScribe extends ConfigAbstract{
    
    private static final Logger logger = Logger.getLogger(ConfigScribe.class);
    
    private static final String DEFAULT_HOST        =   "127.0.0.1";
    private static final int    DEFAULT_PORT        =   1463;
    private static final String DEFAULT_CATEGORY    =   "category";
    private static final int DEFAULT_MAX_CONNECTION = 10;
    private static final int DEFAULT_MAX_CONNECTION_PER_HOST = 5;
    private static final int DEFAULT_TIMEOUT = 1000;
    private static final int DEFAULT_INIT_CONNECTION = 1;
    
    private String host;
    private int port;
    private String category;
    private int maxConnection;
    private int maxConnectionPerHost;
    private int timeout;
    private int initConnection;

    public ConfigScribe(String pathFile) throws ConfigException {
        
        super(pathFile, constant.ScribeConstantString.SCRIBE_NODE);
        
        this.host = this.prefs.get(ScribeConstantString.HOST, DEFAULT_HOST);
        this.port = this.prefs.getInt(ScribeConstantString.HOST, DEFAULT_PORT);
        this.category = this.prefs.get(ScribeConstantString.CATEGORY, DEFAULT_CATEGORY);
        this.maxConnection = this.prefs.getInt(ScribeConstantString.MAX_CONNECTION, DEFAULT_MAX_CONNECTION);
        this.maxConnectionPerHost = this.prefs.getInt(ScribeConstantString.MAX_CONNECTION_PER_HOST, DEFAULT_MAX_CONNECTION_PER_HOST);
        this.timeout = this.prefs.getInt(ScribeConstantString.TIMEOUT, DEFAULT_TIMEOUT);
        this.initConnection = this.prefs.getInt(ScribeConstantString.INIT_CONNECTION, DEFAULT_INIT_CONNECTION);
        
    }
    
    public ConfigScribe() throws ConfigException {
        super(constant.PathConstant.PATH_TO_SCRIBE_CONFIG_FILE, constant.ScribeConstantString.SCRIBE_NODE);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getCategory() {
        return category;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public int getMaxConnectionPerHost() {
        return maxConnectionPerHost;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getInitConnection() {
        return initConnection;
    }

    @Override
    public void showConfig() {
        
        logger.info("Config scribe:"
                + "\n" + constant.ScribeConstantString.HOST + ":" + this.host
                + "\n" + constant.ScribeConstantString.PORT + ":" + this.port
                + "\n" + constant.ScribeConstantString.CATEGORY + ":" + this.category
                + "\n" + constant.ScribeConstantString.MAX_CONNECTION + ":" + this.maxConnection
                + "\n" + constant.ScribeConstantString.MAX_CONNECTION_PER_HOST + ":" + this.maxConnectionPerHost
                + "\n" + constant.ScribeConstantString.INIT_CONNECTION + ":" + this.initConnection
                + "\n" + constant.ScribeConstantString.TIMEOUT + ":" + this.timeout
        );
    }
    
    @Override
    public void checkConfig() throws ConfigException {
        boolean ret = ScribeServiceClient.getInstance(this.host, this.port, 
                this.host, this.port, 
                this.maxConnection, 
                this.maxConnectionPerHost, 
                this.initConnection, 
                this.timeout).writeLog2(category, "Checking connection...\nOn..." + Calendar.getInstance().toString());
        if(ret == false){
            throw new ConfigException("Cannot connect to Scribe");
        }
    }
}
