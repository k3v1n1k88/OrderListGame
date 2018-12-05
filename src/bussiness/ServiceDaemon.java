/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bussiness;

import configuration.ConfigConsumer;
import configuration.ConfigFactory;
import configuration.ConfigServer;
import exception.ConfigException;
import task.ProcessLogLandingPage;
import task.ProcessLogLogin;
import task.ProcessLogPayment;
import org.apache.log4j.Logger;


/**
 *
 * @author root
 */
public class ServiceDaemon {
    
    private static final Logger logger = Logger.getLogger(ServiceDaemon.class);
    
    public static void main(String[] args) {
        try{
            ListOrderGameServer listOrderGameServer = ListOrderGameServer.getInstance();
            
            // Initilaize config
            ConfigFactory.initConfigConnectionPool(constant.PathConstant.PATH_TO_POOL_CONFIG_FILE);
            ConfigFactory.initConfigConsumer(constant.PathConstant.PATH_TO_CONSUMER_CONFIG_FILE);
            ConfigFactory.initConfigDatabaseLevelDB(constant.PathConstant.PATH_TO_DATABASE_LEVELDB_CONFIG_FILE);
            ConfigFactory.initConfigDatabaseRedis(constant.PathConstant.PATH_TO_DATABASE_REDIS_CONFIG_FILE);
            ConfigFactory.initConfigProducer(constant.PathConstant.PATH_TO_PRODUCER_CONFIG_FILE);
            ConfigFactory.initConfigScribe(constant.PathConstant.PATH_TO_SCRIBE_CONFIG_FILE);
            ConfigFactory.initConfigServer(constant.PathConstant.PATH_TO_SERVER_CONFIG_FILE);
            ConfigFactory.initConfigSystem(constant.PathConstant.PATH_TO_SYSTEM_CONFIG_FILE);
            
            ConfigConsumer configConsumer = ConfigFactory.getConfigConsumer(constant.PathConstant.PATH_TO_CONSUMER_CONFIG_FILE);
            ConfigServer configServer = ConfigFactory.getConfigServer(constant.PathConstant.PATH_TO_SERVER_CONFIG_FILE);
            
            new Thread(listOrderGameServer).start();
            new Thread(new ProcessLogPayment(configConsumer, configServer.getMaxThreadProcess())).start();
            new Thread(new ProcessLogLandingPage(configConsumer, configServer.getMaxThreadProcess())).start();
            new Thread(new ProcessLogLogin(configConsumer, configServer.getMaxThreadProcess())).start();
            
        }catch(ConfigException cex){
            logger.error("Cannot load config from file", cex);
            System.exit(0);
        }
        
    }
}
