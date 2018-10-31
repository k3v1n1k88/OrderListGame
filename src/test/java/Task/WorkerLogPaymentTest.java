/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Task;

import Log.LogLogin;
import Log.LogPayment;
import object.value.database.ScoreValueWrapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import redis.clients.jedis.Jedis;

/**
 *
 * @author root
 */
public class WorkerLogPaymentTest {
    
    public WorkerLogPaymentTest() {
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
     * Test of processLog method, of class WorkerLogPayment.
     */
    @Test
    public void testProcessLog() {
        Jedis jedis =  new Jedis("localhost",6379);
        jedis.auth("nhakhoahoc");
        LogLogin log1 = new LogLogin("1212","4754545","282783723");
        LogPayment log2 = new LogPayment("4754545","282783723",100000);
        try{
            WorkerLogLogin worker1 = new WorkerLogLogin(log1);
            worker1.processLog();        
            String res = jedis.hget(log1.getSession(), log1.getGameID());
            WorkerLogPayment worker2 = new WorkerLogPayment(log2);
            worker2.processLog();

            jedis.select(0);
            res = jedis.hget(log1.getSession(), log1.getGameID());
            ScoreValueWrapper scoreValue = ScoreValueWrapper.parse(res);
            assertEquals(scoreValue.getAmountTotal(),100000);
            assertEquals(scoreValue.getScore(),10);
            assertEquals(scoreValue.getTimesOfLogin(),1L);        
        } finally{
            jedis.select(0);
            jedis.del(log1.getSession());
            jedis.select(1);
            jedis.del(log1.getUserID());
            jedis.select(2);
            jedis.del(log1.getGameID());
        }
    }
    
}
