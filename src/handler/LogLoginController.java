/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import api.ApiOutput;
import api.ApiServlet;
import configuration.ConfigProducer;
import exception.ConfigException;
import object.log.LogLogin;
import message.queue.ProducerLogLogin;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class LogLoginController extends ApiServlet{

    private static final long serialVersionUID = 6535839539762841383L;
    private static final Logger logger = Logger.getLogger(LogLoginController.class);
    
    private static ConfigProducer confifProducer = null;
    
    static {
        try {
            confifProducer = new ConfigProducer(constant.PathConstant.PATH_TO_PRODUCER_CONFIG_FILE);
        } catch (ConfigException ex) {
            logger.info(ex.getMessage());
            logger.error(ex);
            System.exit(0);
        }
    }
    
    @Override
    protected ApiOutput execute(HttpServletRequest req, HttpServletResponse resp) {
        if (!checkValidParam(req, new String[]{ constant.DBConstantString.USER_ID,
                                                constant.DBConstantString.GAME_ID,
                                                constant.DBConstantString.SESSION})) {
            
            return new ApiOutput(ApiOutput.STATUS_CODE.BAD_REQUEST.errorCode, "Parameter is not valid");
            
        }

        String userID = req.getParameter(constant.DBConstantString.USER_ID);
        String gameID = req.getParameter(constant.DBConstantString.GAME_ID);
        String session = req.getParameter(constant.DBConstantString.SESSION);

        LogLogin logLogin = new LogLogin(session,userID,gameID);
        
        try {
            ProducerLogLogin prod = new ProducerLogLogin(constant.KafkaConstantString.TOPIC_LOG_LOGIN,LogLoginController.confifProducer);
            prod.sendLog(logLogin);
        } catch (ConfigException ex) {
            logger.error(ex);
            return new ApiOutput(ApiOutput.STATUS_CODE.SYSTEM_ERROR.errorCode, "System happened error when set up");
        } catch(Exception ex){
            logger.error(ex);
            return new ApiOutput(ApiOutput.STATUS_CODE.REQUEST_TIME_OUT.errorCode, "Cannot push message to kafkaf");
        }
        return new ApiOutput(ApiOutput.STATUS_CODE.SUCCESS.errorCode, ApiOutput.STATUS_CODE.SUCCESS.msg);
    }
    
}
