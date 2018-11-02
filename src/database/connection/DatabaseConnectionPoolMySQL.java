package database.connection;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package DatabaseConnectionPool;
//
//
//import Configuration.ConfigConnectionPool;
//import Constant.PoolConstantString;
//import Constant.PathConstant;
//import java.io.IOException;
//import java.sql.Connection;
//import java.util.Set;
//import org.apache.commons.pool2.impl.AbandonedConfig;
//import org.apache.commons.pool2.impl.GenericObjectPool;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//
//
//
//
//public class DatabaseConnectionPoolMySQL implements DatabaseConnectionPool{
//    
//    private GenericObjectPool pool;
//    
//    public DatabaseConnectionPoolMySQL(DatabaseMySQLConnectionFactory factory) throws IOException{
//        this(factory,new GenericObjectPoolConfig(),new AbandonedConfig());
//    }
//    
//    public DatabaseConnectionPoolMySQL(DatabaseMySQLConnectionFactory factory, GenericObjectPoolConfig config, AbandonedConfig abandonedConfig) throws IOException{
//        
//        this.pool = new GenericObjectPool<DatabaseMySQLConnection>(factory,config,abandonedConfig);
//        
//        ConfigConnectionPool conf = ConfigConnectionPool.getInstance(PathConstant.PATH_TO_POOL_CONFIG_FILE);
//        
//        this.pool.setBlockWhenExhausted(conf.getBooleanParam(PoolConstantString.BLOCK_WHEN_EXHAUSTED));
//        
//        this.pool.setLifo(conf.getBooleanParam(PoolConstantString.RETURN_POLICY));
//        
//        this.pool.setMaxIdle(conf.getIntParam(PoolConstantString.MAX_IDLE));
//        
//        this.pool.setMaxTotal(conf.getIntParam(PoolConstantString.MAX_TOTAL));
//        
//        this.pool.setMaxWaitMillis(conf.getLongParam(PoolConstantString.MAX_WAIT_MILLIS));
//        
//        this.pool.setMinIdle(conf.getIntParam(PoolConstantString.MIN_IDLE));
//        
//        this.pool.setMinEvictableIdleTimeMillis(conf.getLongParam(PoolConstantString.MIN_EVICTABLE_IDLE_TIME_MILLIS));
//        
//        this.pool.setNumTestsPerEvictionRun(conf.getIntParam(PoolConstantString.NUM_TESTS_PER_EVICTION_RUN));
//        
//        this.pool.setSoftMinEvictableIdleTimeMillis(conf.getLongParam(PoolConstantString.SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS));
//        
//        this.pool.setTestOnBorrow(conf.getBooleanParam(PoolConstantString.TEST_ON_BORROW));
//        
//        this.pool.setTestOnReturn(conf.getBooleanParam(PoolConstantString.TEST_ON_RETURN));
//        
//        this.pool.setTestWhileIdle(conf.getBooleanParam(PoolConstantString.TEST_WHILE_IDLE));
//        
//        this.pool.setTimeBetweenEvictionRunsMillis(conf.getLongParam(PoolConstantString.TIME_BETWEEN_EVICTION_RUNS_MILLIS));
//        
//    }    
//
//    @Override
//    public Object borrowObject() throws Exception {
//        return this.pool.borrowObject();
//    }
//
//    @Override
//    public void returnObject(Object obj) {
//        this.pool.returnObject(obj);
//    }
//
//    @Override
//    public void invalidateObject(Object obj) throws Exception {
//        this.pool.invalidateObject(obj);
//    }
//
//    @Override
//    public void close() {
//        if(this.pool != null){
//            this.pool.close();
//        }
//    }
//
//    @Override
//    public void clear() {
//        this.pool.clear();
//    }
//
//    @Override
//    public boolean isClosed() {
//        return this.pool.isClosed();
//    }
//
//    @Override
//    public Set<Object> listAllObject() {
//        return this.pool.listAllObjects();
//    }
//}