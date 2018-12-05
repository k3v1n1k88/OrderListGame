/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import api.ApiOutput;
import api.ApiServlet;
import configuration.ConfigFactory;
import configuration.ConfigProducer;
import exception.ConfigException;
import exception.ParseLogException;
import object.log.LogPayment;
import message.queue.ProducerLogPayment;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class LogPaymentController extends ApiServlet {

    private static final long serialVersionUID = 6535839539762841383L;
    private static final Logger logger = Logger.getLogger(LogPaymentController.class);
    
    private static ConfigProducer confifProducer = null;
    
    static {
        try {
            confifProducer = ConfigFactory.getConfigProducer(constant.PathConstant.PATH_TO_PRODUCER_CONFIG_FILE);
        } catch (ConfigException ex) {
            logger.error(ex);
        }
    }
    
    @Override
    protected ApiOutput execute(HttpServletRequest req, HttpServletResponse resp) {
        if (!checkValidParam(req, new String[]{ constant.DBConstantString.USER_ID,
                                                constant.DBConstantString.GAME_ID,
                                                constant.DBConstantString.AMOUNT
        })) {
            
            return new ApiOutput(ApiOutput.STATUS_CODE.BAD_REQUEST.errorCode, "Parameter is not valid");
            
        }

        String userID = req.getParameter(constant.DBConstantString.USER_ID);
        String gameID = req.getParameter(constant.DBConstantString.GAME_ID);
        String amountString = req.getParameter(constant.DBConstantString.AMOUNT);

        LogPayment logPayment;
        
        try {
            logPayment= new LogPayment(userID,gameID,amountString);
            
            ProducerLogPayment prod = new ProducerLogPayment(constant.KafkaConstantString.TOPIC_LOG_PAYMENT, LogPaymentController.confifProducer);
            prod.sendLog(logPayment);
        } catch (ConfigException ex) {
            logger.error(ex.getMessage(),ex);
            return new ApiOutput(ApiOutput.STATUS_CODE.SYSTEM_ERROR.errorCode, "System happened error when set up");
        } catch(ParseLogException pex){
            logger.error(pex.getMessage(),pex);
            return new ApiOutput(ApiOutput.STATUS_CODE.BAD_REQUEST.errorCode, "Parameter is not valid");
        } 
        catch(Exception ex){
            logger.error(ex.getMessage(),ex);
            return new ApiOutput(ApiOutput.STATUS_CODE.REQUEST_TIME_OUT.errorCode, "Cannot push message to kafkaf");
        }
        return new ApiOutput(ApiOutput.STATUS_CODE.SUCCESS.errorCode, ApiOutput.STATUS_CODE.SUCCESS.msg);
    }

}
