/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import Configuration.ConfigConnectionPool;
import Constant.PathConstant;
import Exception.ConfigException;
import Exception.PoolException;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 *
 * @author root
 */
public class DatabaseConnectionPoolLevelDB implements DatabaseConnectionPool<DatabaseLevelDBConnection>{
    
    private GenericObjectPool<DatabaseLevelDBConnection> internalPool;
    
    private ConfigConnectionPool config;
    
    public DatabaseConnectionPoolLevelDB(DatabaseLevelDBConnectionFactory factory, ConfigConnectionPool config) throws ConfigException{
        
        this.config = config;
        
        this.internalPool = new GenericObjectPool<>(factory);
        
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
    
    public DatabaseConnectionPoolLevelDB(DatabaseLevelDBConnectionFactory factory) throws ConfigException{
        this(factory,new ConfigConnectionPool());
    }

    @Override
    public DatabaseLevelDBConnection borrowObjectFromPool() throws PoolException {
        try{
            return this.internalPool.borrowObject();
        } catch (Exception ex) {
            throw new PoolException("Cannot borrow object from pool",ex);
        }
    }

    @Override
    public void returnObjectToPool(DatabaseLevelDBConnection resource) throws PoolException{
        if(resource != null){
            try{
                this.internalPool.returnObject(resource);
            }catch(Exception ex){
                invalidateObjectOfPool(resource);
                throw new PoolException("Cannot return object to pool",ex);
            }
        }
    }

    @Override
    public void invalidateObjectOfPool(DatabaseLevelDBConnection source) throws PoolException {
        try {
            this.internalPool.invalidateObject(source);
        } catch (Exception ex) {
            throw new PoolException("Cannot invalidate object of pool",ex);
        }
    }

    @Override
    public void closePool() throws PoolException {
        if(this.internalPool != null){
            try{
                this.internalPool.close();
            }catch(Exception ex){
                throw new PoolException("Cannot closr pool",ex);
            }
        }
    }

    @Override
    public void clearPool() throws PoolException {
        if(this.internalPool != null){
            try{
                this.internalPool.clear();
            }catch(Exception ex){
                throw new PoolException("Cannot clear pool",ex);
            }
        }
    }

    @Override
    public boolean isClosedPool() {
        return this.internalPool.isClosed();
    }

}
