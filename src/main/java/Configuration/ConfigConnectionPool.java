/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuration;

import Constant.PoolConstantString;
import Constant.PathConstantString;
import com.sun.istack.internal.logging.Logger;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.prefs.Preferences;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import scala.collection.mutable.HashTable;

/**
 *
 * @author root
 */
public class ConfigConnectionPool {

    private static final long serialVersionUID = 498594584985L;
    private static final Logger LOGGER = Logger.getLogger(ConfigConnectionPool.class);
    
    private static Hashtable holderConfig = null;
    
    private Properties config = null;

    
    private ConfigConnectionPool(String path) throws IOException{
        try {
            Ini ini = new Ini(new File(path));
            Preferences pref = new IniPreferences(ini).node(PoolConstantString.CONNECTION_POOL);

            this.config = new Properties();
            
            this.config.put(PoolConstantString.BLOCK_WHEN_EXHAUSTED, pref.getBoolean(PoolConstantString.BLOCK_WHEN_EXHAUSTED, true));
            this.config.put(PoolConstantString.EVICTOR_SHUTDOWN_TIMEOUT_MILLIS, pref.getLong(PoolConstantString.EVICTOR_SHUTDOWN_TIMEOUT_MILLIS, 10000));
            this.config.put(PoolConstantString.FAIRNESS, pref.getBoolean(PoolConstantString.FAIRNESS, false));
            this.config.put(PoolConstantString.MAX_WAIT_MILLIS, pref.getLong(PoolConstantString.MAX_WAIT_MILLIS, -1L));
            this.config.put(PoolConstantString.MIN_EVICTABLE_IDLE_TIME_MILLIS, pref.getLong(PoolConstantString.MIN_EVICTABLE_IDLE_TIME_MILLIS, 1800000L));
            this.config.put(PoolConstantString.MAX_TOTAL, pref.getInt(PoolConstantString.MAX_TOTAL, 100));
            this.config.put(PoolConstantString.MAX_IDLE, pref.getInt(PoolConstantString.MAX_IDLE, 8));
            this.config.put(PoolConstantString.MIN_IDLE, pref.getInt(PoolConstantString.MIN_IDLE, 2));
            this.config.put(PoolConstantString.MAX_TOTAL_PER_KEY, pref.getInt(PoolConstantString.MAX_TOTAL_PER_KEY, 8));
            this.config.put(PoolConstantString.MIN_IDLE_PER_KEY, pref.getInt(PoolConstantString.MIN_IDLE_PER_KEY, 0));
            this.config.put(PoolConstantString.MAX_IDLE_PER_KEY, pref.getInt(PoolConstantString.MAX_IDLE_PER_KEY, 8));
            this.config.put(PoolConstantString.NUM_TESTS_PER_EVICTION_RUN, pref.getInt(PoolConstantString.NUM_TESTS_PER_EVICTION_RUN, 3));
            this.config.put(PoolConstantString.SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS, pref.getLong(PoolConstantString.SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS, -1L));
            this.config.put(PoolConstantString.TEST_ON_CREATE, pref.getBoolean(PoolConstantString.TEST_ON_CREATE, false));
            this.config.put(PoolConstantString.TEST_ON_BORROW, pref.getBoolean(PoolConstantString.TEST_ON_BORROW, false));
            this.config.put(PoolConstantString.TEST_ON_RETURN, pref.getBoolean(PoolConstantString.TEST_ON_RETURN, false));
            this.config.put(PoolConstantString.TEST_WHILE_IDLE, pref.getBoolean(PoolConstantString.TEST_WHILE_IDLE, false));
            this.config.put(PoolConstantString.TIME_BETWEEN_EVICTION_RUNS_MILLIS, pref.getLong(PoolConstantString.TIME_BETWEEN_EVICTION_RUNS_MILLIS, -1));
            this.config.put(PoolConstantString.RETURN_POLICY, pref.getBoolean(PoolConstantString.RETURN_POLICY, true));
            
        } catch (IOException ex) {
            LOGGER.info("Cannot find file in: "+path);
            throw ex;
        }
    }
    
    public static ConfigConnectionPool getInstance(String path) throws IOException{
        if(ConfigConnectionPool.holderConfig == null){
            synchronized(ConfigConnectionPool.class){
                ConfigConnectionPool.holderConfig = new Hashtable();
            }
        }
        if (!holderConfig.contains(path)) {
            synchronized (ConfigConnectionPool.class) {
                ConfigConnectionPool.holderConfig.put(path, new ConfigConnectionPool(path));
            }
        }
        
        ConfigConnectionPool config = (ConfigConnectionPool) ConfigConnectionPool.holderConfig.get(path);
        LOGGER.info("Pool connection configuration:"
                +"\nblock_when_exhausted: "+config.getConfig().get(PoolConstantString.BLOCK_WHEN_EXHAUSTED)
                +"\nevictor_shutdown_timeout_millis:"+config.getConfig().get(PoolConstantString.EVICTOR_SHUTDOWN_TIMEOUT_MILLIS)
                +"\nfairness: "+config.getConfig().get(PoolConstantString.FAIRNESS)
                +"\nmax_wait_millis: "+config.getConfig().get(PoolConstantString.MAX_WAIT_MILLIS)
                +"\nmin_evictable_idle_time_millis: "+config.getConfig().get(PoolConstantString.MIN_EVICTABLE_IDLE_TIME_MILLIS)
                +"\nmax_total: "+config.getConfig().get(PoolConstantString.MAX_TOTAL)
                +"\nmax_idle: "+config.getConfig().get(PoolConstantString.MAX_IDLE)
                +"\nmin_idle: "+config.getConfig().get(PoolConstantString.MIN_IDLE)
                +"\nmax_total_per_key: "+config.getConfig().get(PoolConstantString.MAX_TOTAL_PER_KEY)
                +"\nmin_idle_per_key: "+config.getConfig().get(PoolConstantString.MIN_IDLE_PER_KEY)
                +"\nmax_idle_per_key: "+config.getConfig().get(PoolConstantString.MAX_IDLE_PER_KEY)
                +"\nnum_tests_per_eviction_run: "+config.getConfig().get(PoolConstantString.NUM_TESTS_PER_EVICTION_RUN)
                +"\nsoft_min_evictable_idle_time_millis: "+config.getConfig().getProperty(PoolConstantString.SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS)
                +"\ntest_on_create: "+config.getConfig().get(PoolConstantString.TEST_ON_CREATE)
                +"\ntest_on_borrow: "+config.getConfig().get(PoolConstantString.TEST_ON_BORROW)
                +"\ntest_on_return: "+config.getConfig().get(PoolConstantString.TEST_ON_RETURN)
                +"\ntest_while_idle: "+config.getConfig().get(PoolConstantString.TEST_WHILE_IDLE)
                +"\ntime_between_eviction_runs_millis: "+config.getConfig().get(PoolConstantString.TIME_BETWEEN_EVICTION_RUNS_MILLIS)
                +"\nreturn_policy: "+config.getConfig().get(PoolConstantString.RETURN_POLICY));
        
        return config;
    }
    
    public Long getLongParam(String param){
        Long res = (Long)this.config.get(param);
        return res;
    }
    
    public Integer getIntParam(String param){
        Integer res = (Integer) this.config.get(param);
        return res;
    }
    
    public String getStringParam(String param){
        String res = (String) this.config.get(param);
        return res;
    }
    
    public Boolean getBooleanParam(String param){
        try {
            Boolean res = Boolean.parseBoolean(String.valueOf(this.config.get(param)));
            return res;
        } catch (NumberFormatException e){
            LOGGER.info("Cast bay roi em oi");
            throw e;
        }
    }
          
    public Properties getConfig(){
        return this.config;
    } 
}
