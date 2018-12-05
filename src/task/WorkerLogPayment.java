/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;


import database.connection.DatabaseLevelDBConnection;
import database.connection.DatabaseRedisConnection;
import exception.DatabaseException;
import exception.PoolException;
import object.log.LogPayment;
import org.apache.log4j.Logger;
import zcore.utilities.ScribeServiceClient;

/**
 *
 * @author root
 */
public class WorkerLogPayment extends WorkerAbstract<LogPayment> {

    private static final Logger logger = Logger.getLogger(WorkerLogPayment.class);

    public WorkerLogPayment(LogPayment log) {
        super(log);
    }

    @Override
    protected boolean processLog() {
        DatabaseRedisConnection dbRediscnn = null;
        DatabaseLevelDBConnection dbLevelDBcnn = null;
        try {
            // Get connection
            dbRediscnn = poolRedis.borrowObjectFromPool();
            boolean res = this.log.access(dbRediscnn);
            
            // If missing mapping
            if(res == false){
                WorkerLogPayment.getScribeClient().writeLog2(configScribe.getCategory(), this.log.parse2String());
            }
        } catch (PoolException ex) {
            logger.error(ex.getMessage(),ex);
            WorkerLogPayment.getScribeClient().writeLog2("LogPaymentErrorByPool", this.log.parse2String());
        } catch (DatabaseException ex) {
            logger.error(ex.getMessage(),ex);
            WorkerLogPayment.getScribeClient().writeLog2("LogPaymentErrorAccessDatabase", this.log.parse2String());
        } finally {
            try {
                if (dbRediscnn != null) {
                    poolRedis.returnObjectToPool(dbRediscnn);
                }
            } catch (PoolException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return false;
    }

}
