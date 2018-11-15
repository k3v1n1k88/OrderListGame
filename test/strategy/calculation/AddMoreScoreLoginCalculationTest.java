/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy.calculation;

import exception.CalculationException;
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
public class AddMoreScoreLoginCalculationTest {
    
    public AddMoreScoreLoginCalculationTest() {
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
     * Test of calculatePoint method, of class AddMoreScoreLoginCalculation.
     */
    @Test
    public void testCalculatePoint() {
        try {
            AddMoreScoreLoginCalculation cal = new AddMoreScoreLoginCalculation(4,1,604801);
            assertEquals(cal.calculatePoint(),3);
        } catch (CalculationException ex) {
            fail("Not expected exception at here");
        }
    }
    
}
