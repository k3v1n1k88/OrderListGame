/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import constant.ScribeConstantString;
import exception.ConfigException;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ConfigScribe extends ConfigurationAbstract{
    
    private static final Logger logger = Logger.getLogger(ConfigScribe.class);
    
    private static final String DEFAULT_HOST        =   "127.0.0.1";
    private static final int    DEFAULT_PORT        =   1463;
    private static final String DEFAULT_CATEGORY    =   "cetegory";
    
    private String host;
    
    private int port;
    
    private String cetegory;

    public ConfigScribe(String pathFile) throws ConfigException {
        
        super(pathFile, constant.ScribeConstantString.SCRIBE_NODE);
        
        this.host = this.prefs.get(ScribeConstantString.HOST, DEFAULT_HOST);
        this.port = this.prefs.getInt(ScribeConstantString.HOST, DEFAULT_PORT);
        this.cetegory = this.prefs.get(ScribeConstantString.CATEGORY, DEFAULT_CATEGORY);
        
        logger.info("Config scribe:"
                + "\n" + constant.ScribeConstantString.HOST + ":" + this.host
                + "\n" + constant.ScribeConstantString.PORT + ":" + this.port
                + "\n" + constant.ScribeConstantString.CATEGORY + ":" + this.cetegory
        );
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

    public String getCetegory() {
        return cetegory;
    }
    
}
