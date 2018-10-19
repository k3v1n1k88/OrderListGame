/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import redis.clients.jedis.Jedis;

/**
 *
 * @author root
 */
public class DatabaseRedisConnectionTest {
    
    public DatabaseRedisConnectionTest() {
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
     * Test of createConnection method, of class DatabaseRedisConnection.
     */
    @Test
    public void testPing() {
        try {
            DatabaseRedisConnection dbcnn = new DatabaseRedisConnection(new DatabaseInfo("localhost",6379,"GameListOrder","root","nhakhoahoc"));
            dbcnn.createConnection();
            String res = dbcnn.getConnection().ping();
            assertEquals("Excpected a \"PONG\" but got \""+res+"\"",res,"PONG");
            dbcnn.close();
        } catch (Exception ex) {
            fail("Expected a connection but got a unvalid connection+\n"+ex);
        }
    }
    
    @Test
    public void testConnectionSet(){
        try {
            DatabaseRedisConnection dbcnn = new DatabaseRedisConnection(new DatabaseInfo("localhost",6379,"GameListOrder","root","nhakhoahoc"));
            dbcnn.createConnection();
            Jedis jedis = dbcnn.getConnection();
            jedis.set("abc", "123");
            String res = jedis.get("abc");
            assertEquals("Excpected a \"123\" but got \""+res+"\"",res,"123");
            Long r = jedis.del("abc");
            r= jedis.del("123");
            dbcnn.close();
        } catch (Exception ex) {
            fail("Expected a connection but got a unvalid connection+\n"+ex);
        }
    }
    
    @Test
    public void testConnectionHSet(){
        try {
            DatabaseRedisConnection dbcnn = new DatabaseRedisConnection(new DatabaseInfo("localhost",6379,"GameListOrder","root","nhakhoahoc"));
            dbcnn.createConnection();
            Jedis jedis = dbcnn.getConnection();
            jedis.hset("abc", "123", "!@#");
            jedis.hset("abc", "456", "$%^");
            String res = jedis.hget("abc", "123");
            assertEquals("Expected value \"!@#\" but got \""+res+"\"",res,"!@#");
            dbcnn.close();
        } catch (Exception ex) {
            fail("Expected a connection but got a unvalid connection+\n"+ex);
        }
    }

    /**
     * Test of close method, of class DatabaseRedisConnection.
     */
    @Test
    public void testClose(){
        try {
            DatabaseRedisConnection dbcnn = new DatabaseRedisConnection(new DatabaseInfo("localhost",6379,"GameListOrder","root","nhakhoahoc"));
            dbcnn.createConnection();
            dbcnn.close();
            String res = dbcnn.getConnection().ping();
            assertEquals("Expected a result not \"PONG\"",res,"PONG");
        } catch (Exception ex) {
            fail("Expected a connection but got a unvalid connection+\n"+Arrays.toString(ex.getStackTrace()));
        }
    }

}
