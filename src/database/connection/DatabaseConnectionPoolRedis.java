/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connection;

import configuration.ConfigConnectionPool;
import configuration.ConfigDatabaseRedis;
import constant.PathConstant;
import exception.ConfigException;
import exception.PoolException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import redis.clients.util.Pool;

/**
 *
 * @author root
 */
public class DatabaseConnectionPoolRedis extends Pool<DatabaseRedisConnection> implements DatabaseConnectionPool<DatabaseRedisConnection>{
   
    private static final Logger logger =Logger.getLogger(DatabaseConnectionPoolRedis.class);
    
    private ConfigConnectionPool config;
        
    public DatabaseConnectionPoolRedis(DatabaseRedisConnectionFactory factory, ConfigConnectionPool config) throws ConfigException{
        
        this.internalPool = new GenericObjectPool<>(factory);
        
        if(config == null){
            logger.error("Config is null");
            throw new ConfigException("config must have not null");
        }
        
        this.config = config;
        
        this.internalPool.setBlockWhenExhausted(this.config.isBlockWhenExhausted());
        
        this.internalPool.setLifo(this.config.isLifo());
        
        this.internalPool.setMaxIdle(this.config.getMaxIdle());
        
        this.internalPool.setMaxTotal(this.config.getMaxTotal());
        
        this.internalPool.setMaxWaitMillis(this.config.getMaxWaitMillis());
        
        this.internalPool.setMinIdle(this.config.getMinIdle());
        
        this.internalPool.setMinEvictableIdleTimeMillis(this.config.getMinEvictableIdleTimeMillis());
        
        this.internalPool.setNumTestsPerEvictionRun(this.config.getNumTestsPerEvictionRun());
        
        this.internalPool.setSoftMinEvictableIdleTimeMillis(this.config.getSoftMinEvictableIdleTimeMillis());
        
        this.internalPool.setTestOnBorrow(this.config.isTestOnBorrow());
        
        this.internalPool.setTestOnReturn(this.config.isTestOnReturn());
        
        this.internalPool.setTestWhileIdle(this.config.isTestWhileIdle());
        
        this.internalPool.setTimeBetweenEvictionRunsMillis(this.config.getTimeBetweenEvictionRunsMillis());     
    } 
    
    public DatabaseConnectionPoolRedis(DatabaseRedisConnectionFactory factory) throws ConfigException{
        this(factory,new ConfigConnectionPool());
    }

    public ConfigConnectionPool getConfig() {
        return config;
    }
    
    @Override
    public DatabaseRedisConnection borrowObjectFromPool() throws PoolException{
        try{
            return this.getResource();
        }catch(Exception ex){
            throw new PoolException("Cannot get object from pool",ex);
        }
    }

    @Override
    public void returnObjectToPool(DatabaseRedisConnection resource) throws PoolException{
        if(resource != null){
            try{
                resource.getConnection().resetState();
                this.internalPool.returnObject(resource);
            }catch(Exception e){
                invalidateObjectOfPool(resource);
                throw new PoolException("Cannot return reosurce to Jedis pool",e);
            }
        }
    }

    @Override
    public void invalidateObjectOfPool(DatabaseRedisConnection obj) throws PoolException {
        try {
            this.internalPool.invalidateObject(obj);
        } catch (Exception e) {
            throw new PoolException("Cannot invalidate object from poll",e);
        }
    }

    @Override
    public void closePool() throws PoolException {
        try {
            this.internalPool.close();
        } catch (Exception e) {
            throw new PoolException("Could not close the pool", e);
        }
    }

    @Override
    public void clearPool() {
        this.internalPool.clear();
    }
    
    public int getNumsObjectWaiter(){
        return this.getNumWaiters();
    }
    
    public int getNumsObjectIdle(){
        return this.getNumIdle();
    }
    
    public int getNumsObjectActive(){
        return this.getNumActive();
    }
    
    @Override
    public boolean isClosedPool() {
        return this.internalPool.isClosed();
    }

}
