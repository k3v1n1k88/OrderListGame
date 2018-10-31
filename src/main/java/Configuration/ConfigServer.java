/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuration;

import Exception.ConfigException;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ConfigServer extends ConfigurationAbstract{
    
    private static final Logger logger = Logger.getLogger(ConfigServer.class);
    
    private String host;
    private int port;
    
    public ConfigServer(String pathFileConfig) throws ConfigException{
        super(pathFileConfig,Constant.ServerConstantString.SERVER_NODE);
        
        this.host = this.prefs.get(Constant.ServerConstantString.HOST_CONFIG, ConfigServer.DEFAULT_HOST);
        this.port = this.prefs.getInt(Constant.ServerConstantString.PORT_CONFIG, ConfigServer.DEFAULT_PORT);
        
        System.out.println("Config of server:"
            + "\n" + Constant.ServerConstantString.HOST_CONFIG + ":" + this.host
            + "\n" + Constant.ServerConstantString.PORT_CONFIG + ":" + this.port
        );
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
    
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 55100;
}
