/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageQueue;

import Constant.PathConstantString;
import java.io.IOException;
import java.util.Properties;
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
public class LocalProducerConfigTest {
    
    public LocalProducerConfigTest() {
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
     * Test of getInstance method, of class ProducerConfig.
     */
    @Test
    public void testGetInstanceWithValidFilePath() {
        try {
            LocalProducerConfig prodConf = LocalProducerConfig.getInstance(PathConstantString.PATH_TO_PRODUCER_CONFIG_FILE);
            Properties prop = prodConf.getConfig();
            assertTrue("Expexted ack config, but cannot got it",prop.containsKey(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG));
            assertTrue("Expexted bootstrap server, but cannot got it",prop.containsKey(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
            assertTrue("Expexted linger ms, but cannot got it",prop.containsKey(org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG));
        } catch (IOException ex) {
            fail("Not expected error here, please check config file path");
        }
    }
    
    @Test
    public void testGetInstanceWithUnValidFilePath() {
        try {
            LocalProducerConfig prodConf = LocalProducerConfig.getInstance("123");
            Properties prop = prodConf.getConfig();
            fail("Expected program exit");
        } catch (IOException ex) {
            
        }
    }
}
