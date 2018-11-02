/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connection.pool;

import configuration.ConfigConnectionPool;
import database.connection.DatabaseConnectionPoolRedis;
import database.connection.DatabaseRedisConnection;
import database.connection.DatabaseRedisConnectionFactory;
import exception.ConfigException;
import exception.PoolException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class DatabaseConnectionPoolRedisTest {
    
    public DatabaseConnectionPoolRedisTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getConfig method, of class DatabaseConnectionPoolRedis.
     */
    @Test
    public void testGetConfig() {
    }

    /**
     * Test of borrowObject method, of class DatabaseConnectionPoolRedis.
     */
    @Test
    public void testBorrowObject() {
        try {
            DatabaseRedisConnectionFactory factory = new DatabaseRedisConnectionFactory();
            DatabaseConnectionPoolRedis pool = new DatabaseConnectionPoolRedis(factory);
            DatabaseRedisConnection dbcnn = pool.borrowObjectFromPool();
            assertEquals("PONG",dbcnn.getConnection().ping());
        } catch (ConfigException ex) {
            fail("Error when read config file");
        } catch (PoolException ex) {
            fail("Expected can borrow a valid connection but cannot got it");
        } catch (Exception e) {
            fail("Expected can borrow a valid connection but cannot got it");
        }
    }

    /**
     * Test of returnObject method, of class DatabaseConnectionPoolRedis.
     */
    @Test
    public void testReturnObject() {
        try {
            DatabaseRedisConnectionFactory factory = new DatabaseRedisConnectionFactory();
            DatabaseConnectionPoolRedis pool = new DatabaseConnectionPoolRedis(factory);
            DatabaseRedisConnection dbcnn = pool.borrowObjectFromPool();
            assertEquals("PONG",dbcnn.getConnection().ping());
            pool.returnObjectToPool(dbcnn);
        } catch (ConfigException ex) {
            fail("Expected can config pool, but cannot");
        } catch (PoolException ex) {
            fail("Expected can return object to pool but cannot got it");
        } catch (Exception e) {
            fail("Expected can return object to pool but cannot got it");
        }
    }

    /**
     * Test of invalidateObject method, of class DatabaseConnectionPoolRedis.
     */
    @Test
    public void testInvalidateObject() throws Exception {
    }

    /**
     * Test of close method, of class DatabaseConnectionPoolRedis.
     */
    @Test
    public void testClose() {
    }

    /**
     * Test of clear method, of class DatabaseConnectionPoolRedis.
     */
    @Test
    public void testClear() {
    }
    

    /**
     * Test of listAllObject method, of class DatabaseConnectionPoolRedis.
     */
    @Test
    public void testListAllObject() {
    }
    
}
