/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;


import database.connection.DatabaseRedisConnection;
import exception.DatabaseException;
import exception.PoolException;
import object.log.LogLandingPage;
import org.apache.log4j.Logger;
import zcore.utilities.ScribeServiceClient;

/**
 *
 * @author root
 */
public class WorkerLogLandingPage extends WorkerAbstract<LogLandingPage>{

    private static final Logger logger = Logger.getLogger(WorkerLogLandingPage.class);
    
    public WorkerLogLandingPage(LogLandingPage log){
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
                ).writeLog2("LogLandingPagetErrorByPool", this.log.parse2String());
        } catch (DatabaseException ex) {
            logger.error(ex.getMessage(),ex);
            WorkerLogLandingPage.getScribeClient().writeLog2("LogLandingPagetErrorByAccessDatabase", this.log.parse2String());
        } finally{
            try {
                if(dbcnn != null)
                    poolRedis.returnObjectToPool(dbcnn);
            } catch (PoolException ex) {
                logger.error(ex.getMessage());
            }
        }
        return false;
    }
    
}
