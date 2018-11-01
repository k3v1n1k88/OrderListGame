///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package Strategy.DatabaseMappingMissing;
//
//import Exception.DatabaseException;
//import Log.LogPayment;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import org.iq80.leveldb.*;
//import static org.fusesource.leveldbjni.JniDBFactory.*;
//import java.io.*;
//import static org.junit.Assert.fail;
//
///**
// *
// * @author root
// */
//public class DatabaseMappingMissingLevelDBTest {
//    
//    public DatabaseMappingMissingLevelDBTest() {
//    }
//    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }
//
//    /**
//     * Test of writeLogToDatabase method, of class DatabaseMappingMissingLevelDB.
//     */
//    @Test
//    public void testWriteLogToDatabase() {
//        try {
//            String databaseTest = "missing-mapping-test";
//            DatabaseMappingMissingLevelDB dbmm = new DatabaseMappingMissingLevelDB(databaseTest);
//            dbmm.writeLogToDatabase(new LogPayment("123","346",313));
//            factory.destroy(new File(databaseTest), new Options());
//            
//        } catch (DatabaseException | IOException ex) {
//            fail("Fail when test write to database");
//        }
//    }
//    
//}
