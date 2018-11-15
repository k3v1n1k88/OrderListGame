/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cache.CacheLandingPage.ListGame;
import exception.ConfigException;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
    }

    /**
     * Test of getInstance method, of class CacheLandingPage.
     */
    @Test
    public void testGetInstance() throws Exception {
    }

    /**
     * Test of getListRecommendation method, of class CacheLandingPage.
     */
    @Test
    public void testGetListRecommendation() throws ConfigException, ExecutionException {
        CacheLandingPage cache = CacheLandingPage.getInstance();
        ListGame listSc = cache.getList("nr7fi2ZZag8SEL29jaCY19BvpWwZE09JNJfebAgSBbCB6V4UW003bPjXIe8uIy8PxFe9qZ2kCPWHrhD8FVa9e7t01470hMBqX9qMGQMx0IxFXlUHRTxl9HhKuJGPrfn56i5K2eKwUP803nI4TAbNvF");
        
        System.out.println("List Scoring: ");
        
        for(String s: listSc.getScoringList()){
            System.out.println(s);
        }
        
        System.out.println("List recommendation of this game: ");
        System.out.println(listSc.getRecommandatioList().size());
        for(String s: listSc.getRecommandatioList()){
            System.out.println(s);
        }
        
        System.out.println("List recommendation: ");
        List<String> listRe = cache.getListRecommendation();
        System.out.println(listRe.size());
        for(String s: listRe){
            System.out.println(s);
        }
    }
    
}
