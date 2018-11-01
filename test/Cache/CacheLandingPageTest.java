/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cache;

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
public class CacheLandingPageTest {
    
    public CacheLandingPageTest() {
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
     * Test of getList method, of class CacheLandingPage.
     */
    @Test
    public void testGetList() throws Exception {
        CacheLandingPage cache = new CacheLandingPage();
        CacheLandingPage.ListGame listGame = cache.getList("123424");
        System.out.println(listGame.getRecommandatioList());
        System.out.println(listGame.getScoringList());
    }
    
}
