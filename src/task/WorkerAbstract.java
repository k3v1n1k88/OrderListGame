/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;

import configuration.ConfigConnectionPool;
import configuration.ConfigFactory;
import configuration.ConfigScribe;
import configuration.ConfigSystem;
import database.connection.DatabaseConnectionPool;
import database.connection.DatabaseConnectionPoolRedis;
import database.connection.DatabaseRedisConnection;
import database.connection.DatabaseRedisConnectionFactory;
import exception.ConfigException;
import object.log.Log;
import org.apache.log4j.Logger;
import zcore.utilities.ScribeServiceClient;

/**
 *
 * @author root
 * @param <L> instance of Log
 */
public abstract class WorkerAbstract<L extends Log> implements Runnable{
    
    private static final Logger logger = Logger.getLogger(WorkerAbstract.class);
    
    protected static ConfigSystem configSystem;
    protected static ConfigConnectionPool configPool;
    protected static ConfigScribe configScribe;
    protected static DatabaseConnectionPool<DatabaseRedisConnection> poolRedis;
    
    static {
        try {
            configScribe = ConfigFactory.getConfigScribe(constant.PathConstant.PATH_TO_SCRIBE_CONFIG_FILE);
            configPool = ConfigFactory.getConfigConnectionPool(constant.PathConstant.PATH_TO_POOL_CONFIG_FILE);
            configSystem = ConfigFactory.getConfigSystem(constant.PathConstant.PATH_TO_SYSTEM_CONFIG_FILE);
            DatabaseRedisConnectionFactory factory = new DatabaseRedisConnectionFactory();
            poolRedis = new DatabaseConnectionPoolRedis(factory,configPool);
        } catch (ConfigException ex) {
            logger.error(ex);
        }
    }
    
    protected L log;
    
    public WorkerAbstract(L log){
        this.log = log;
    }
    
    public static ScribeServiceClient getScribeClient(){
        return ScribeServiceClient.getInstance(configScribe.getHost(),
                        configScribe.getPort(),
                        configScribe.getHost(),
                        configScribe.getPort(),
                        configScribe.getMaxConnection(),
                        configScribe.getMaxConnectionPerHost(),
                        configScribe.getInitConnection(),
                        configScribe.getTimeout()
                );
    }
    
    @Override
    public void run() {
        processLog();
    }
    
    protected abstract boolean processLog();
}
