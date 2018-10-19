/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageQueue;

import Constant.PathConstantString;
import java.io.IOException;
import java.util.Properties;
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
public class LocalConsumerConfigTest {
    
    public LocalConsumerConfigTest() {
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
     * Test of getInstance method, of class ConsumerConfig.
     */
    @Test
    public void testGetInstanceWithValidFilePath() {
        try {
            LocalConsumerConfig prodConf = LocalConsumerConfig.getInstance(PathConstantString.PATH_TO_CONSUMER_CONFIG_FILE);
            Properties prop = prodConf.getConfig();
            assertTrue("Expexted ack config, but cannot got it",prop.containsKey(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG));
            assertTrue("Expexted bootstrap server, but cannot got it",prop.containsKey(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        } catch (IOException ex) {
            fail("Not expected error here, please check config file path");
        }
    }
    
    @Test
    public void testGetInstanceWithUnValidFilePath() {
        try {
            LocalConsumerConfig prodConf = LocalConsumerConfig.getInstance("123");
            Properties prop = prodConf.getConfig();
            fail("Expected program exit");
        } catch (IOException ex) {
            
        }
    }

    /**
     * Test of getInstance method, of class LocalConsumerConfig.
     */
    @Test
    public void testGetInstance() throws Exception {
    }

    /**
     * Test of getConfig method, of class LocalConsumerConfig.
     */
    @Test
    public void testGetConfig() {
    }
    
}
