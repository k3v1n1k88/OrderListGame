/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Task;

import Exception.PoolException;
import Log.LogLogin;
import java.util.List;
import object.value.database.ScoreValueWrapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;

/**
 *
 * @author root
 */
public class WorkerLogLoginTest {
    
    public WorkerLogLoginTest() {
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
     * Test of processLog method, of class WorkerLogLogin.
     */
    @Test
    public void tesNewLogLogin() throws PoolException {
        Jedis jedis =  new Jedis("localhost",6379);
        jedis.auth("nhakhoahoc");
        
        LogLogin log = new LogLogin("1212","4754545","282783723");
        try{
            WorkerLogLogin worker = new WorkerLogLogin(log);
            worker.processLog();
            jedis.select(0);
            String res = jedis.hget(log.getSession(), log.getGameID());
            ScoreValueWrapper scoreValue = ScoreValueWrapper.parse(res);
            assertEquals(scoreValue.getAmountTotal(),0);
            assertEquals(scoreValue.getScore(),1);
            assertEquals(scoreValue.getTimesOfLogin(),1);
            jedis.select(1);
            String res2 = jedis.get(log.getUserID());
            assertEquals(res2,"1212");
            jedis.select(2);
            String res3 = jedis.hget((Constant.DBConstantString.GAME_ID+":").concat(log.getGameID()),Constant.DBConstantString.POINT);
            assertEquals(res3,"0");
        }finally{
            jedis.select(0);
            jedis.del(log.getSession());
            jedis.select(1);
            jedis.del(log.getUserID());
            jedis.select(2);
            jedis.del((Constant.DBConstantString.GAME_ID+":").concat(log.getGameID()));
        }
    }
    
    @Test
    public void tesMultiLogLogin() throws PoolException {
        Jedis jedis =  new Jedis("localhost",6379);
        jedis.auth("nhakhoahoc");
        
        LogLogin log1 = new LogLogin("1212","4754545","282783723");
        LogLogin log2 = new LogLogin("1212","4754545","124656");
        LogLogin log3 = new LogLogin("1212","4754545","124656");
        LogLogin log4 = new LogLogin("123446","121212335","287381");
        
        try{
            jedis.select(0);
            
            WorkerLogLogin worker1 = new WorkerLogLogin(log1);
            worker1.processLog();
            
            WorkerLogLogin worker2 = new WorkerLogLogin(log2);
            worker2.processLog();
            
            WorkerLogLogin worker3 = new WorkerLogLogin(log3);
            worker3.processLog();
            
            WorkerLogLogin worker4 = new WorkerLogLogin(log4);
            worker4.processLog();
            
            ScoreValueWrapper scoreValue;
            String res;
            
            res = jedis.hget(log1.getSession(), log1.getGameID());
            scoreValue = ScoreValueWrapper.parse(res);
            assertEquals(scoreValue.getAmountTotal(),0);
            assertEquals(scoreValue.getScore(),1);
            assertEquals(scoreValue.getTimesOfLogin(),1);
            
            res = jedis.hget(log2.getSession(), log2.getGameID());
            scoreValue = ScoreValueWrapper.parse(res);
            assertEquals(scoreValue.getAmountTotal(),0);
            assertEquals(scoreValue.getScore(),2);
            assertEquals(scoreValue.getTimesOfLogin(),2);
            
            res = jedis.hget(log4.getSession(), log4.getGameID());
            scoreValue = ScoreValueWrapper.parse(res);
            assertEquals(scoreValue.getAmountTotal(),0);
            assertEquals(scoreValue.getScore(),1);
            assertEquals(scoreValue.getTimesOfLogin(),1);
            
            jedis.select(1);
            String res1 = jedis.get(log1.getUserID());
            String res2 = jedis.get(log4.getUserID());
            assertEquals(res1,"1212");
            assertEquals(res2,"123446");
            
            jedis.select(2);
            res1 = jedis.hget((Constant.DBConstantString.GAME_ID+":").concat(log1.getGameID()),Constant.DBConstantString.POINT);
            res2 = jedis.hget((Constant.DBConstantString.GAME_ID+":").concat(log2.getGameID()),Constant.DBConstantString.POINT);
            String res3 = jedis.hget((Constant.DBConstantString.GAME_ID+":").concat(log4.getGameID()),Constant.DBConstantString.POINT);
//            List<String> sortedList = jedis.sort(Constant.DBConstantString.LIST_GAME,new SortingParams().
//                    by(Constant.DBConstantString.GAME_ID+":*->".concat(Constant.DBConstantString.POINT))
//                    );
//            System.out.println(sortedList);
            assertEquals(res1,"1");
            assertEquals(res2,"1");
            assertEquals(res3,"0");
        }finally{
            jedis.select(0);
            jedis.del(log1.getSession());
            jedis.del(log2.getSession());
            jedis.del(log3.getSession());
            jedis.del(log4.getSession());
            
            jedis.select(1);
            jedis.del(log1.getUserID());
            jedis.del(log2.getSession());
            jedis.del(log4.getSession());
            
            jedis.select(2);
            jedis.del((Constant.DBConstantString.GAME_ID+":").concat(log1.getGameID()));
            jedis.del((Constant.DBConstantString.GAME_ID+":").concat(log2.getGameID()));
            jedis.del((Constant.DBConstantString.GAME_ID+":").concat(log3.getGameID()));
            jedis.del((Constant.DBConstantString.GAME_ID+":").concat(log4.getGameID()));
            jedis.del("all:".concat(Constant.DBConstantString.GAME_ID));
        }
    }
    
}
