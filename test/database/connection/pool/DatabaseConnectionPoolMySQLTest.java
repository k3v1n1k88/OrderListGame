package database.connection.pool;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package DatabaseConnectionPool;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
///**
// *
// * @author root
// */
//public class DatabaseConnectionPoolMySQLTest {
//    private static DatabaseConnectionPoolMySQL pool;
//    
//    public DatabaseConnectionPoolMySQLTest() {
//        
//    }
//    
//    @BeforeClass
//    public static void setUpClass() throws IOException {
//        DatabaseMySQLConnectionFactory factory = new DatabaseMySQLConnectionFactory("localhost",
//                7777,
//                "GameListOrder",
//                "root",
//                "nhakhoahoc");
//        pool = new DatabaseConnectionPoolMySQL(factory);
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//        
//    }
//    
//    @After
//    public void tearDown() {
//        
//    }
//
//    /**
//     * Test of borrowObject method, of class DatabaseConnectionPoolMySQL.
//     */
//    @Test
//    public void testBorrowObject() {
//        try {
//            DatabaseMySQLConnection dbcnn = (DatabaseMySQLConnection) pool.borrowObject();
//            Connection cnn = null;
//            cnn = dbcnn.getConnection();
//            assertTrue(cnn.isClosed() != true);
//            pool.returnObject(dbcnn);
//            cnn = dbcnn.getConnection();
//            assertTrue(cnn.isClosed() != true);
//            pool.returnObject(dbcnn);
//            cnn = dbcnn.getConnection();
//            assertTrue(cnn.isClosed() != true);
//            pool.returnObject(dbcnn);
//        } catch (SQLException ex) {
//           fail("expected a connection valid, but got a fail connection");
//        } catch (Exception ex) {
//           fail("expected got a connection, but cannot got anything");
//        }
//    }
//
//    /**
//     * Test of returnObject method, of class DatabaseConnectionPoolMySQL.
//     */
//    @Test
//    public void testReturnObject() {
//        try {
//            DatabaseMySQLConnection dbcnn = (DatabaseMySQLConnection) pool.borrowObject();
//            Connection cnn = null;
//            cnn = dbcnn.getConnection();
//            cnn.isClosed();
//            pool.returnObject(dbcnn);
//            dbcnn.getConnection().isClosed();
//        } catch (SQLException ex) {
//            fail("expected a connection valid, but got a fail connection");
//        } catch (Exception ex) {
//            fail("expected got a connection, but cannot got anything");
//        }
//        
//    }
//
//    /**
//     * Test of invalidateObject method, of class DatabaseConnectionPoolMySQL.
//     */
//    @Test
//    public void testInvalidateObject() throws Exception {
//        
//    }
//
//    /**
//     * Test of close method, of class DatabaseConnectionPoolMySQL.
//     */
//    @Test
//    public void testClose() {
//
//    }
//
//    /**
//     * Test of clear method, of class DatabaseConnectionPoolMySQL.
//     */
//    @Test
//    public void testClear() {
//    }
//
//    /**
//     * Test of isClosed method, of class DatabaseConnectionPoolMySQL.
//     */
//    @Test
//    public void testIsClosed() {
//    }
//
//    /**
//     * Test of listAllObject method, of class DatabaseConnectionPoolMySQL.
//     */
//    @Test
//    public void testListAllObject() {
//    }
//    
//}
