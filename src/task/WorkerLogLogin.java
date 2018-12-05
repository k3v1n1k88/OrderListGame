/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;

import strategy.calculation.AddMoreScoreLoginCalculation;
import strategy.calculation.PointCalculation;
import constant.SystemConstant;
import database.connection.DatabaseRedisConnection;
import database.connection.DatabaseRedisConnectionFactory;
import exception.CalculationException;
import exception.ConfigException;
import exception.DatabaseException;
import exception.PoolException;
import object.log.LogLogin;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import object.value.database.ScoreValueWrapper;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import static task.WorkerAbstract.configScribe;
import zcore.utilities.ScribeServiceClient;

/**
 *
 * @author root
 */
public class WorkerLogLogin extends WorkerAbstract<LogLogin> {
    
    private static final Logger logger = Logger.getLogger(WorkerLogLogin.class);
    
    public WorkerLogLogin(LogLogin log){
        super(log);
    }

    @Override
    protected boolean processLog() {
        DatabaseRedisConnection dbcnn = null;
        try {
            
            dbcnn = poolRedis.borrowObjectFromPool();
            this.log.access(dbcnn);
            
        } catch (PoolException ex) {
            logger.error(ex.getMessage(),ex);
            ScribeServiceClient.getInstance(configScribe.getHost(),
                        configScribe.getPort(),
                        configScribe.getHost(),
                        configScribe.getPort(),
                        configScribe.getMaxConnection(),
                        configScribe.getMaxConnectionPerHost(),
                        configScribe.getInitConnection(),
                        configScribe.getTimeout()
                ).writeLog2("LogLogintErrorByPool", this.log.parse2String());
        } catch (DatabaseException ex) {
            logger.error(ex.getMessage(),ex);
            WorkerLogLogin.getScribeClient().writeLog2("LogLogintErrorByAccessDatabase", this.log.parse2String());
        } finally{
            if(dbcnn!=null){
                try {
                    poolRedis.returnObjectToPool(dbcnn);
                } catch (PoolException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        return false;
        
    }
}

