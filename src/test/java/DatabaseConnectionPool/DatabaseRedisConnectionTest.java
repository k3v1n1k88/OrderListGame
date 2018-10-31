/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
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
    public void testConnectionWithString(){
        try {
            DatabaseRedisConnection dbcnn = new DatabaseRedisConnection(new DatabaseInfo("localhost", 6379, "GameListOrder", "root", "nhakhoahoc"));
            dbcnn.createConnection();
            Jedis jedis = dbcnn.getConnection();
            jedis.ping();
            jedis.set("abc", "123");
            String res = jedis.get("abc");
            assertEquals("123",res);
            jedis.del("abc");
            dbcnn.close();
        } catch (Exception ex) {
            fail("Expected a connection but got a unvalid connection+\n"+ex);
        }
    }
    
    @Test
    public void testConnectionWithHSet(){
        try {
            DatabaseRedisConnection dbcnn = new DatabaseRedisConnection(new DatabaseInfo("localhost", 6379, "GameListOrder", "root", "nhakhoahoc"));
            dbcnn.createConnection();
            Jedis jedis = dbcnn.getConnection();
            jedis.hset("user#1", "name", "Peter");
            jedis.hset("user#1", "job", "politician");

            String name = jedis.hget("user#1", "name");
            Map<String, String> fields = jedis.hgetAll("user#1");
            String job = fields.get("job");
            assertEquals("politician",job);
            assertEquals("Peter",name);
            jedis.del("user#1");
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
    }

    /**
     * Test of createConnection method, of class DatabaseRedisConnection.
     */
    @Test
    public void testCreateConnection() throws Exception {
    }

    /**
     * Test of getConnection method, of class DatabaseRedisConnection.
     */
    @Test
    public void testGetConnection() {
    }
    
    @Test
    public void testSelectDatabase(){
        DatabaseRedisConnection dbcnn = new DatabaseRedisConnection(new DatabaseInfo("localhost", 6379, "GameListOrder", "root", "nhakhoahoc"));
        dbcnn.createConnection();
        dbcnn.selectDatabase(0);
        Jedis jedis = dbcnn.getConnection();
        jedis.set("abc", "123");
        String res = jedis.get("abc");
        assertEquals("123", res);
        jedis.select(1);
        res = jedis.get("abc");
        assertFalse("123".equals(res));
        jedis.select(0);
        jedis.del("abc");
        dbcnn.close();
    }

}
