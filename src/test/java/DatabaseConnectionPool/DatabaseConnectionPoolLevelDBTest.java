/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.iq80.leveldb.*;
import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;
import java.util.logging.Level;

/**
 *
 * @author root
 */
public class DatabaseConnectionPoolLevelDBTest {
    
    public DatabaseConnectionPoolLevelDBTest() {
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

    @Test
    public void testCreatePool() throws IOException, Exception {
        String databaseName = "abc";
        DatabaseConnectionPoolLevelDB dbcnnPool = null;
        try {
            DatabaseLevelDBConnectionFactory dbFactory = new DatabaseLevelDBConnectionFactory(new DatabaseInfo(databaseName));
            dbcnnPool = new DatabaseConnectionPoolLevelDB(dbFactory);
            assertFalse(dbcnnPool == null);
            dbcnnPool.close();
            factory.destroy(new File(databaseName), new Options());
        } catch (IOException ex) {
            fail("Create pool failure with databaseName: "+databaseName);
            throw ex;
        }
    }
    @Test
    public void testGetConnectionFromPool() throws IOException{
        String databaseName = "abc";
        String key = "abc";
        String value = "123";
        String res = "";
        DatabaseConnectionPoolLevelDB dbcnnPool = null;
        DB db = null;
        
        DatabaseLevelDBConnectionFactory dbFactory = new DatabaseLevelDBConnectionFactory(new DatabaseInfo(databaseName));
        dbcnnPool = new DatabaseConnectionPoolLevelDB(dbFactory);
        try {
            DatabaseLevelDBConnection dbcnn = (DatabaseLevelDBConnection) dbcnnPool.borrowObject();
            db =  dbcnn.getConnection();
            db.put((bytes(key)), bytes(value));
            res = asString(db.get(bytes(key)));
            assertEquals(res,value);
            db.close();
        } catch (Exception ex) {
            fail("Cannot get connection from pool");
        }
        dbcnnPool.close();
        factory.destroy(new File(databaseName), new Options());
    }
}
