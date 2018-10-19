/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import Configuration.ConfigConnectionPool;
import Constant.PoolConstantString;
import Constant.PathConstantString;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 *
 * @author root
 */
public class DatabaseConnectionPoolLevelDB implements DatabaseConnectionPool{
    
    private GenericObjectPool pool;
    
    public DatabaseConnectionPoolLevelDB(DatabaseLevelDBConnectionFactory factory) throws IOException{
        this(factory, PathConstantString.PATH_TO_POOL_CONFIG_FILE);
    }
    
    public DatabaseConnectionPoolLevelDB(DatabaseLevelDBConnectionFactory factory, String pathPoolConfig) throws IOException{
        this(factory,new GenericObjectPoolConfig(),new AbandonedConfig(),pathPoolConfig);
    }
    
    public DatabaseConnectionPoolLevelDB(DatabaseLevelDBConnectionFactory factory, GenericObjectPoolConfig config, AbandonedConfig abandonedConfig, String pathPoolConfig) throws IOException{
        
        this.pool = new GenericObjectPool<DatabaseLevelDBConnection>(factory,config,abandonedConfig);
        
        ConfigConnectionPool conf = ConfigConnectionPool.getInstance(pathPoolConfig);
        
        this.pool.setBlockWhenExhausted(conf.getBooleanParam(PoolConstantString.BLOCK_WHEN_EXHAUSTED));
        
        this.pool.setLifo(conf.getBooleanParam(PoolConstantString.RETURN_POLICY));
        
        this.pool.setMaxIdle(conf.getIntParam(PoolConstantString.MAX_IDLE));
        
        this.pool.setMaxTotal(conf.getIntParam(PoolConstantString.MAX_TOTAL));
        
        this.pool.setMaxWaitMillis(conf.getLongParam(PoolConstantString.MAX_WAIT_MILLIS));
        
        this.pool.setMinIdle(conf.getIntParam(PoolConstantString.MIN_IDLE));
        
        this.pool.setMinEvictableIdleTimeMillis(conf.getLongParam(PoolConstantString.MIN_EVICTABLE_IDLE_TIME_MILLIS));
        
        this.pool.setNumTestsPerEvictionRun(conf.getIntParam(PoolConstantString.NUM_TESTS_PER_EVICTION_RUN));
        
        this.pool.setSoftMinEvictableIdleTimeMillis(conf.getLongParam(PoolConstantString.SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS));
        
        this.pool.setTestOnBorrow(conf.getBooleanParam(PoolConstantString.TEST_ON_BORROW));
        
        this.pool.setTestOnReturn(conf.getBooleanParam(PoolConstantString.TEST_ON_RETURN));
        
        this.pool.setTestWhileIdle(conf.getBooleanParam(PoolConstantString.TEST_WHILE_IDLE));
        
        this.pool.setTimeBetweenEvictionRunsMillis(conf.getLongParam(PoolConstantString.TIME_BETWEEN_EVICTION_RUNS_MILLIS));     
    
    } 

    @Override
    public Object borrowObject() throws Exception {
        return pool.borrowObject();
    }

    @Override
    public void returnObject(Object obj) {
        this.pool.returnObject((DatabaseLevelDBConnection) obj);
    }

    @Override
    public void invalidateObject(Object obj) throws Exception{
        this.pool.invalidateObject((DatabaseLevelDBConnection) obj);
    }

    @Override
    public void close() {
        if(this.pool != null){
            this.pool.close();
        }
    }

    @Override
    public void clear() {
        this.pool.clear();
    }

    @Override
    public boolean isClosed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Object> listAllObject() {
        return new HashSet<>(this.pool.listAllObjects());
    }

}
