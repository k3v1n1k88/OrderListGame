/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import exception.ConfigException;
import java.util.Hashtable;
import java.util.Map;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ConfigServer extends ConfigAbstract{
    
    private static final Logger logger = Logger.getLogger(ConfigServer.class);
    
    private String host;
    private int port;
    private int maxThread;
    private int minThread;
    private int idleTimeout;
    private int maxThreadProcess;
    
    public ConfigServer(String pathFileConfig) throws ConfigException{
        super(pathFileConfig, constant.ServerConstantString.SERVER_NODE);
        
        this.host = prefs.get(constant.ServerConstantString.HOST_CONFIG, ConfigServer.DEFAULT_HOST);
        this.port = prefs.getInt(constant.ServerConstantString.PORT_CONFIG, ConfigServer.DEFAULT_PORT);
        this.maxThread = prefs.getInt(constant.ServerConstantString.MAX_THREAD, ConfigServer.DEFAULT_MAX_THREAD);
        this.minThread = prefs.getInt(constant.ServerConstantString.MIN_THREAD, ConfigServer.DEFAULT_MIN_THREAD);
        this.idleTimeout = prefs.getInt(constant.ServerConstantString.IDLE_TIMEOUT, ConfigServer.DEFAULT_IDLE_TIMEOUT);
        this.maxThreadProcess = prefs.getInt(constant.ServerConstantString.MAX_THREAD_PROCESS, ConfigServer.DEFAULT_MAX_THREAD_PROCESS);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getMaxThread() {
        return maxThread;
    }

    public int getMinThread() {
        return minThread;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public int getMaxThreadProcess() {
        return maxThreadProcess;
    }
    
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 55100;
    private static final int DEFAULT_MAX_THREAD = 100;
    private static final int DEFAULT_MIN_THREAD = 10;
    private static final int DEFAULT_IDLE_TIMEOUT = 123;
    private static final int DEFAULT_MAX_THREAD_PROCESS = 100;

    @Override
    public void showConfig() {
        logger.info("Config of server:"
                + "\n" + constant.ServerConstantString.HOST_CONFIG + ":" + this.host
                + "\n" + constant.ServerConstantString.PORT_CONFIG + ":" + this.port
                + "\n" + constant.ServerConstantString.MAX_THREAD + ":" + this.maxThread
                + "\n" + constant.ServerConstantString.MIN_THREAD + ":" + this.minThread
                + "\n" + constant.ServerConstantString.IDLE_TIMEOUT + ":" + this.idleTimeout
                + "\n" + constant.ServerConstantString.MAX_THREAD_PROCESS + ":" + this.maxThreadProcess
        );
    }
    @Override
    public void checkConfig() throws ConfigException {
        
    }
}
