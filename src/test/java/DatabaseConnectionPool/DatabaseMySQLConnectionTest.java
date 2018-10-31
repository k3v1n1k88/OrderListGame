/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
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
public class DatabaseMySQLConnectionTest {
    
    public DatabaseMySQLConnectionTest() {
        
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
     * Test of makeConnection method, of class DatabaseMySQLConnection.
     */
    @Test
    public void testMakeConnection(){
        try {
            DatabaseMySQLConnection dbcnn = new DatabaseMySQLConnection(new DatabaseInfo("localhost",7777,"GameListOrder","root","nhakhoahoc"));
            dbcnn.createConnection();
            assertFalse("expected a connection but got null",dbcnn.getConnection() == null);
            assertFalse("expected a open connection but got a closed connection",dbcnn.getConnection().isClosed()==true);
        } catch (SQLException ex) {
            fail("expected a connection valid, but cannot got it, may be your database is not valid. Please check it again");
        }
    }

    /**
     * Test of getDatabaseInfo method, of class DatabaseMySQLConnection.
     */
    @Test
    public void testGetDatabaseInfo() {
    }

    /**
     * Test of getConnection method, of class DatabaseMySQLConnection.
     */
    @Test
    public void testGetConnection(){
        try {
            DatabaseMySQLConnection dbcnn = new DatabaseMySQLConnection(new DatabaseInfo("localhost",7777,"GameListOrder","root","nhakhoahoc"));
            dbcnn.createConnection();
            assertFalse("expected a connection but got null",dbcnn.getConnection() == null);
            assertFalse("expected a open connection but got a closed connection",dbcnn.getConnection().isClosed()==true);
        } catch (SQLException ex) {
            fail("expected a connection valid, but cannot got it, may be your database is not valid. Please check it again");
        }
    }
}
