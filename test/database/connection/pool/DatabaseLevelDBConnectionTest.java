/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connection.pool;

import database.connection.DatabaseLevelDBConnection;
import exception.DatabaseException;
import java.io.IOException;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.*;
import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;
import java.util.logging.Level;
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
public class DatabaseLevelDBConnectionTest {
    
    public DatabaseLevelDBConnectionTest() {
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
     * Test of makeConnection method, of class DatabaseLevelDBConnection.
     */
    @Test
    public void testConnectionPut() throws IOException {
        String databaseName = "test";
        String key = "abc";
        String value = "123";
        String res = "";
        DatabaseLevelDBConnection dbcnn = new DatabaseLevelDBConnection("test",true,false,400000000,1000,16,4096,-1,false,false,false);
        try {
            dbcnn.createConnection();
        } catch (DatabaseException ex) {
            fail("Expected success making a connetion, but cannot make it");
        }
        DB db = null;
        try {
            db = dbcnn.getConnection();
            assertFalse(db==null);
            db.put(bytes(key),bytes(value));
            res = asString(db.get(bytes(key)));
            assertEquals(res,value);
            db.close();
            factory.destroy(new File("test"), new Options());
        } catch (IOException ex) {
            fail("Fail when create connection");
            throw ex;
        }
    }

    /**
     * Test of createConnection method, of class DatabaseLevelDBConnection.
     */
    @Test
    public void testCreateConnection() throws IOException, Exception {
        try {
            System.out.println("createConnection");
            DatabaseLevelDBConnection instance = new DatabaseLevelDBConnection("test");
            instance.createConnection();
            boolean res = instance.ping();
            instance.close();
            factory.destroy(new File("test"), new Options());
            assertEquals(res,true);
        } catch (DatabaseException ex) {
            fail("Expected can make connection but cannot make it");
        }
    }

    /**
     * Test of getConnection method, of class DatabaseLevelDBConnection.
     */
    @Test
    public void testGetConnection() throws Exception {
    }

    /**
     * Test of ping method, of class DatabaseLevelDBConnection.
     */
    @Test
    public void testPing() {
    }
    
}
