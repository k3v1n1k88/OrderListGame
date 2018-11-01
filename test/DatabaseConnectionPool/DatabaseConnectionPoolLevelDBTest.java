/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import Exception.ConfigException;
import Exception.PoolException;
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
    public void testCreatePool() {
        String databaseName = "test";
        DatabaseConnectionPoolLevelDB dbcnnPool = null;
        try {
            DatabaseLevelDBConnectionFactory dbFactory = new DatabaseLevelDBConnectionFactory(databaseName);
            dbcnnPool = new DatabaseConnectionPoolLevelDB(dbFactory);
            assertFalse(dbcnnPool == null);
            
            dbcnnPool.closePool();
            factory.destroy(new File(databaseName), new Options());
        } catch (IOException ex) {
            fail("Create pool failure with databaseName: "+databaseName);
        } catch (PoolException ex) {
            fail("Close pool failure when test");
        } catch (ConfigException ex) {
            fail("Expected config valid, but cannot got config from file config");
        }
    }
    @Test
    public void testConnectionGetFromPool(){
        String databaseName = "test";
        String key = "abc";
        String value = "123";
        String res = "";
        DatabaseConnectionPoolLevelDB dbcnnPool = null;
        DB db = null;
        try {
            DatabaseLevelDBConnectionFactory dbFactory = new DatabaseLevelDBConnectionFactory(databaseName);
            dbcnnPool = new DatabaseConnectionPoolLevelDB(dbFactory);
        
            DatabaseLevelDBConnection dbcnn = dbcnnPool.borrowObjectFromPool();
            db =  dbcnn.getConnection();
            db.put((bytes(key)), bytes(value));
            res = asString(db.get(bytes(key)));
            assertEquals(res,value);  
            dbcnnPool.returnObjectToPool(dbcnn);
            dbcnnPool.closePool();
            factory.destroy(new File(databaseName), new Options());
        } catch (ConfigException ex) {
            fail("Config pool get error");
        } catch (PoolException ex) {
            fail("Close pool failure");
        } catch (IOException ex) {
            fail("Cannot delete database after test");
        }
    }
}
