/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import java.io.IOException;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.*;
import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;
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
    public void testMakeConnection() throws IOException {
        String databaseName = "test";
        String key = "abc";
        String value = "123";
        String res = "";
        DatabaseLevelDBConnection dbvnn = new DatabaseLevelDBConnection(new DatabaseInfo(databaseName),new Options().createIfMissing(true));
        dbvnn.createConnection();
        DB db = null;
        try {
            db = dbvnn.getConnection();
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
    
}
