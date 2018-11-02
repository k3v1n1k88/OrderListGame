/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import constant.PoolConstantString;
import exception.ConfigException;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ConfigConnectionPool extends ConfigurationAbstract{

    private static final long serialVersionUID = 498594584985L;
    
    private static final Logger LOGGER = Logger.getLogger(ConfigConnectionPool.class);
    
    private boolean blockWhenExhausted ;
    private long evictorShutdownTimeoutMillis ;
    private boolean fairness ;
    private long maxWaitMillis ;             
    private long minEvictableIdleTimeMillis ;
    private int maxTotal ;
    private int maxIdle ;
    private int minIdle;
    private int maxTotalPerKey ;
    private int minIdlePerKey ;
    private int maxIdlePerKey ;
    private int numTestsPerEvictionRun ;
    private long softMinEvictableIdleTimeMillis ;
    private boolean testOnCreate ;
    private boolean testOnBorrow ;
    private boolean testOnReturn ;
    private boolean testWhileIdle ;
    private long timeBetweenEvictionRunsMillis ;
    private boolean lifo ;

    
    public ConfigConnectionPool(String path) throws ConfigException{
        
        super(path,PoolConstantString.CONNECTION_POOL);
        
        this.blockWhenExhausted = this.prefs.getBoolean(PoolConstantString.BLOCK_WHEN_EXHAUSTED, DEFAULT_BLOCK_WHEN_EXHAUSTED);
        this.evictorShutdownTimeoutMillis = this.prefs.getLong(PoolConstantString.EVICTOR_SHUTDOWN_TIMEOUT_MILLIS, DEFAULT_EVICTOR_SHUTDOWN_TIMEOUT_MILLIS);
        this.fairness = this.prefs.getBoolean(PoolConstantString.FAIRNESS, DEFAULT_FAIRNESS);
        this.maxWaitMillis = this.prefs.getLong(PoolConstantString.MAX_WAIT_MILLIS, DEFAULT_MAX_WAIT_MILLIS);
        this.minEvictableIdleTimeMillis = this.prefs.getLong(PoolConstantString.MIN_EVICTABLE_IDLE_TIME_MILLIS, DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS);
        this.maxTotal = this.prefs.getInt(PoolConstantString.MAX_TOTAL, DEFAULT_MAX_TOTAL);
        this.maxIdle = this.prefs.getInt(PoolConstantString.MAX_IDLE, DEFAULT_MAX_IDLE);
        this.minIdle = this.prefs.getInt(PoolConstantString.MIN_IDLE, DEFAULT_MIN_IDLE);
        this.maxTotalPerKey = this.prefs.getInt(PoolConstantString.MAX_TOTAL_PER_KEY, DEFAULT_MAX_TOTAL_PER_KEY);
        this.minIdlePerKey = this.prefs.getInt(PoolConstantString.MIN_IDLE_PER_KEY, DEFAULT_MIN_IDLE_PER_KEY);
        this.maxIdlePerKey= this.prefs.getInt(PoolConstantString.MAX_IDLE_PER_KEY, DEFAULT_MAX_IDLE_PER_KEY);
        this.numTestsPerEvictionRun = this.prefs.getInt(PoolConstantString.NUM_TESTS_PER_EVICTION_RUN, DEFAULT_NUM_TESTS_PER_EVICTION_RUN);
        this.softMinEvictableIdleTimeMillis = this.prefs.getLong(PoolConstantString.SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS, DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS);
        this.testOnCreate = this.prefs.getBoolean(PoolConstantString.TEST_ON_CREATE, DEFAULT_TEST_ON_CREATE);
        this.testOnBorrow = this.prefs.getBoolean(PoolConstantString.TEST_ON_BORROW, DEFAULT_TEST_ON_BORROW);
        this.testOnReturn = this.prefs.getBoolean(PoolConstantString.TEST_ON_RETURN, DEFAULT_TEST_ON_RETURN);
        this.testWhileIdle = this.prefs.getBoolean(PoolConstantString.TEST_WHILE_IDLE, DEFAULT_TEST_WHILE_IDLE);
        this.timeBetweenEvictionRunsMillis = this.prefs.getLong(PoolConstantString.TIME_BETWEEN_EVICTION_RUNS_MILLIS, DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
        this.lifo = this.prefs.getBoolean(PoolConstantString.RETURN_POLICY, DEFAULT_RETURN_POLICY);
        
        System.out.println("Pool connection configuration:"
                + "\nblockWhenExhausted: " + this.blockWhenExhausted
                + "\nevictorShutdownTimeoutMillis:" + this.evictorShutdownTimeoutMillis
                + "\nfairness: " + this.fairness
                + "\nmaxWaitMillis: " + this.maxWaitMillis
                + "\nminEvictableIdleTimeMillis: " + this.minEvictableIdleTimeMillis
                + "\nmaxTotal: " + this.maxTotal
                + "\nmaxIdle: " + this.maxIdle
                + "\nminIdle: " + this.minIdle
                + "\nmaxTotalPerKey: " + this.maxTotalPerKey
                + "\nminIdlePerKey: " + this.minIdlePerKey
                + "\nmaxIdlePerKey: " + this.maxIdlePerKey
                + "\nnumTestsPerEvictionRun: " + this.numTestsPerEvictionRun
                + "\nsoftMinEvictableIdleTimeMillis: " + this.softMinEvictableIdleTimeMillis
                + "\ntestOnCreate: " + this.testOnCreate
                + "\ntestOnBorrow: " + this.testOnBorrow
                + "\ntestOnReturn: " + this.testOnReturn
                + "\ntestWhileIdle: " + this.testWhileIdle
                + "\ntimeBetweenEvictionRunsMillis: " + this.timeBetweenEvictionRunsMillis
                + "\nreturnPolicy: " + this.lifo);
    }

    public ConfigConnectionPool() throws ConfigException {
        this(constant.PathConstant.PATH_TO_POOL_CONFIG_FILE);
    }

    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    public long getEvictorShutdownTimeoutMillis() {
        return evictorShutdownTimeoutMillis;
    }

    public boolean isFairness() {
        return fairness;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public int getMaxTotalPerKey() {
        return maxTotalPerKey;
    }

    public int getMinIdlePerKey() {
        return minIdlePerKey;
    }

    public int getMaxIdlePerKey() {
        return maxIdlePerKey;
    }

    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public long getSoftMinEvictableIdleTimeMillis() {
        return softMinEvictableIdleTimeMillis;
    }

    public boolean isTestOnCreate() {
        return testOnCreate;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public boolean isLifo() {
        return lifo;
    }   
    
    private static boolean DEFAULT_BLOCK_WHEN_EXHAUSTED =  true;
    private static long DEFAULT_EVICTOR_SHUTDOWN_TIMEOUT_MILLIS = 10000L;
    private static boolean DEFAULT_FAIRNESS = false;
    private static long DEFAULT_MAX_WAIT_MILLIS = -1L;             
    private static long DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 1800000L;
    private static int DEFAULT_MAX_TOTAL = 100;
    private static int DEFAULT_MAX_IDLE = 8;
    private static int DEFAULT_MIN_IDLE =2;
    private static int DEFAULT_MAX_TOTAL_PER_KEY = 8;
    private static int DEFAULT_MIN_IDLE_PER_KEY = 0;
    private static int DEFAULT_MAX_IDLE_PER_KEY = 8;
    private static int DEFAULT_NUM_TESTS_PER_EVICTION_RUN = 3;
    private static long DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = -1;
    private static boolean DEFAULT_TEST_ON_CREATE = false;
    private static boolean DEFAULT_TEST_ON_BORROW = false;
    private static boolean DEFAULT_TEST_ON_RETURN = false;
    private static boolean DEFAULT_TEST_WHILE_IDLE = false;
    private static long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = -1;
    private static boolean DEFAULT_RETURN_POLICY = true;
    
}
