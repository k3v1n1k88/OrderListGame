/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.value.database;

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
public class ScoreValueWrapperTest {
    
    public ScoreValueWrapperTest() {
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
     * Test of parse method, of class ScoreValueWrapper.
     */
    @Test
    public void testParse() {
        String source = "{\"score\":1000,\"timesOfLogin\":1201,\"amountTotal\":121212,\"latestLogin\":188}";
        ScoreValueWrapper obj = ScoreValueWrapper.parse(source);
        assertEquals(obj.getAmountTotal(),121212L);
        assertEquals(obj.getScore(),1000);
        assertEquals(obj.getTimesOfLogin(),1201L);
        assertEquals(obj.getLatestLogin(),188L);
    }

    /**
     * Test of toJSONString method, of class ScoreValueWrapper.
     */
    @Test
    public void testToJSONString() {
        String source = "{\"score\":1000,\"timesOfLogin\":1201,\"amountTotal\":121212,\"latestLogin\":188}";
        ScoreValueWrapper obj1 = ScoreValueWrapper.parse(source);
        assertEquals(source,obj1.toJSONString());
    }
    
}
