/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuration;

import Exception.ConfigException;
import java.util.logging.Level;
import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author root
 */
public class ConfigDatabaseRedisTest {
    
    public ConfigDatabaseRedisTest() {
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
    public void testReadConfigFile(){
        try {
            ConfigDatabaseRedis config = new ConfigDatabaseRedis(Constant.PathConstant.PATH_TO_DATABASE_REDIS_CONFIG_FILE);
        } catch (ConfigException ex) {
            fail("Expected can read config file but cannot got it");
        }
    }
    @Test
    public void testReadParamFromConfigFile(){
        try {
            ConfigDatabaseRedis config = new ConfigDatabaseRedis(Constant.PathConstant.PATH_TO_DATABASE_REDIS_CONFIG_FILE);
            assertEquals(config.getHost(),"127.0.0.2");
            assertEquals(config.getPort(),6379);
            assertEquals(config.getPassword(),"nhakhoahoc");
            assertEquals(config.getConnectionTimeoutMillius(),2000);
            assertEquals(config.getSoTimeoutMillius(),2000);
        } catch (ConfigException ex) {
            fail("Expected can read config file but cannot got it");
        }
    }
    
}
