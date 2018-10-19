/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuration;

/**
 *
 * @author root
 */
public final class ConfigOfConnectionPool {
    
    public static final boolean BLOCK_WHEN_EXHAUSTED = true;
    
    public static final long EVICTOR_SHUTDOWN_TIMEOUT_MILLIS = 10L * 1000L;
    
    private static final boolean FAIRNESS = false;
    
    private static final boolean LIFO = true;

    public static final long MAX_WAIT_MILLIS = -1L;

    public static final long MIN_EVICTABLE_IDLE_TIME_MILLIS = 1000L * 60L * 30L;
    
    public static final int MAX_TOTAL = 100;
    
    public static final int MAX_IDLE = 8;

    public static final int MIN_IDLE = 2;
    
    public static final int MAX_TOTAL_PER_KEY = 8;

    public static final int MIN_IDLE_PER_KEY = 0;

    public static final int MAX_IDLE_PER_KEY = 8;

    public static final int NUM_TESTS_PER_EVICTION_RUN = 3;
    
    public static final long SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = -1;

    public static final boolean TEST_ON_CREATE = false;

    public static final boolean TEST_ON_BORROW = false;

    public static final boolean TEST_ON_RETURN = false;

    public static final boolean TEST_WHILE_IDLE = false;

    public static final long TIME_BETWEEN_EVICTION_RUNS_MILLIS = -1L;
        
    public static final boolean RETURN_POLICY = LIFO;
    
    private ConfigOfConnectionPool(){}
}
