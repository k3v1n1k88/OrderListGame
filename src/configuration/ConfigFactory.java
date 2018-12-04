/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import exception.ConfigException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import message.queue.KafkaLogAbstract;

/**
 *
 * @author root
 */
public class ConfigFactory {
    
    private static Map<String, ConfigConnectionPool> holderConfigConnectionPool;
    private static Map<String, ConfigConsumer> holderConfigConsumer;
    private static Map<String, ConfigDatabaseLevelDB> holderConfigDatabaseLevelDB;
    private static Map<String, ConfigDatabaseRedis> holderConfigDatabaseRedis;
    private static Map<String, ConfigProducer> holderConfigProducer;
    private static Map<String, ConfigScribe> holderConfigScribe;
    private static Map<String, ConfigServer> holderConfigServer;
    private static Map<String, ConfigSystem> holderConfigSystem;
    
    static{
        if(holderConfigConnectionPool == null){
            holderConfigConnectionPool = new Hashtable<>();
        }
        if(holderConfigConsumer == null){
            holderConfigConsumer = new Hashtable<>();
        }
        if(holderConfigDatabaseLevelDB == null){
            holderConfigDatabaseLevelDB = new Hashtable<>();
        }
        if(holderConfigDatabaseRedis == null){
            holderConfigDatabaseRedis = new Hashtable<>();
        }
        if(holderConfigProducer == null){
            holderConfigProducer = new Hashtable<>();
        }
        if(holderConfigScribe == null){
            holderConfigScribe = new Hashtable<>();
        }
        if(holderConfigServer == null){
            holderConfigServer = new Hashtable<>();
        }
        if(holderConfigSystem == null){
            holderConfigSystem = new Hashtable<>();
        }
    }
    
    // Create config
    public static ConfigConnectionPool getConfigConnectionPool(String pathFile) throws ConfigException{
        if(!holderConfigConnectionPool.containsKey(pathFile)){
            initConfigConnectionPool(pathFile);
        }
        return holderConfigConnectionPool.get(pathFile);
    }
    
    public static ConfigConsumer getConfigConsumer(String pathFile) throws ConfigException{
        if(!holderConfigConsumer.containsKey(pathFile)){
            initConfigConsumer(pathFile);
        }
        return holderConfigConsumer.get(pathFile);
    }
    
    public static ConfigDatabaseLevelDB getConfigDatabaseLevelDB(String pathFile) throws ConfigException{
        if(!holderConfigDatabaseLevelDB.containsKey(pathFile)){
            initConfigDatabaseLevelDB(pathFile);
        }
        return holderConfigDatabaseLevelDB.get(pathFile);
    }
    
    public static ConfigDatabaseRedis getConfigDatabaseRedis(String pathFile) throws ConfigException{
        if(!holderConfigDatabaseRedis.containsKey(pathFile)){
            initConfigDatabaseRedis(pathFile);
        }
        return holderConfigDatabaseRedis.get(pathFile);
    }
    
    public static ConfigProducer getConfigProducer(String pathFile) throws ConfigException{
        if(!holderConfigProducer.containsKey(pathFile)){
            initConfigProducer(pathFile);
        }
        return holderConfigProducer.get(pathFile);
    }
    
    public static ConfigScribe getConfigScribe(String pathFile) throws ConfigException{
        if(!holderConfigScribe.containsKey(pathFile)){
            initConfigScribe(pathFile);
        }
        return holderConfigScribe.get(pathFile);
    }
    
    public static ConfigServer getConfigServer(String pathFile) throws ConfigException{
        if(!holderConfigServer.containsKey(pathFile)){
            initConfigServer(pathFile);
        }
        return holderConfigServer.get(pathFile);
    }
    
    public static ConfigSystem getConfigSystem(String pathFile) throws ConfigException{
        if(!holderConfigSystem.containsKey(pathFile)){
            initConfigSystem(pathFile);
        }
        return holderConfigSystem.get(pathFile);
    }
    
    //Initialize Config
    public static void initConfigConnectionPool(String pathFile) throws ConfigException{
        if(!holderConfigConnectionPool.containsKey(pathFile)){
            ConfigConnectionPool newConfig= new ConfigConnectionPool(pathFile);
            newConfig.showConfig();
            holderConfigConnectionPool.put(pathFile, newConfig);
        }
    }
    
    public static void initConfigConsumer(String pathFile) throws ConfigException{
        if(!holderConfigConsumer.containsKey(pathFile)){
            ConfigConsumer newConfig= new ConfigConsumer(pathFile);
            newConfig.showConfig();
            newConfig.checkConfig();
            holderConfigConsumer.put(pathFile, newConfig);
        }
    }
    
    public static void initConfigDatabaseLevelDB(String pathFile) throws ConfigException{
        if(!holderConfigDatabaseLevelDB.containsKey(pathFile)){
            ConfigDatabaseLevelDB newConfig= new ConfigDatabaseLevelDB(pathFile);
            newConfig.showConfig();
            newConfig.checkConfig();
            holderConfigDatabaseLevelDB.put(pathFile, newConfig);
        }
    }
    
    public static void initConfigDatabaseRedis(String pathFile) throws ConfigException{
        if(!holderConfigDatabaseRedis.containsKey(pathFile)){
            ConfigDatabaseRedis newConfig= new ConfigDatabaseRedis(pathFile);
            newConfig.showConfig();
            newConfig.checkConfig();
            holderConfigDatabaseRedis.put(pathFile, newConfig);
        }
    }
    
    public static void initConfigProducer(String pathFile) throws ConfigException{
        if(!holderConfigProducer.containsKey(pathFile)){
            ConfigProducer newConfig= new ConfigProducer(pathFile);
            newConfig.showConfig();
            KafkaLogAbstract.checkConnect(newConfig.getBootstrapServers(), 10000, 5000);
            holderConfigProducer.put(pathFile, newConfig);
        }
    }
    
    public static void initConfigScribe(String pathFile) throws ConfigException{
        if(!holderConfigScribe.containsKey(pathFile)){
            ConfigScribe newConfig= new ConfigScribe(pathFile);
            newConfig.showConfig();
            newConfig.checkConfig();
            holderConfigScribe.put(pathFile, newConfig);
        }
    }
    
    public static void initConfigServer(String pathFile) throws ConfigException{
        if(!holderConfigServer.containsKey(pathFile)){
            ConfigServer newConfig= new ConfigServer(pathFile);
            newConfig.showConfig();
            holderConfigServer.put(pathFile, newConfig);
        }
    }
    
    public static void initConfigSystem(String pathFile) throws ConfigException{
        if(!holderConfigSystem.containsKey(pathFile)){
            ConfigSystem newConfig= new ConfigSystem(pathFile);
            newConfig.showConfig();
            holderConfigSystem.put(pathFile, newConfig);
        }
    }
    
}
