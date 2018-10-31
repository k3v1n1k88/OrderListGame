/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Handler;

import API.ApiOutput;
import API.ApiServlet;
import Configuration.ConfigProducer;
import Exception.ConfigException;
import Exception.ParseLogException;
import Log.LogLogin;
import MessageQueue.ProducerLogLogin;
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
            confifProducer = new ConfigProducer(Constant.PathConstant.PATH_TO_PRODUCER_CONFIG_FILE);
        } catch (ConfigException ex) {
            logger.info(ex.getMessage());
            logger.error(ex);
            System.exit(0);
        }
    }
    
    @Override
    protected ApiOutput execute(HttpServletRequest req, HttpServletResponse resp) {
        if (!checkValidParam(req, new String[]{ Constant.DBConstantString.USER_ID,
                                                Constant.DBConstantString.GAME_ID,
                                                Constant.DBConstantString.SESSION})) {
            
            return new ApiOutput(ApiOutput.STATUS_CODE.BAD_REQUEST.errorCode, "Parameter is not valid");
            
        }

        String userID = req.getParameter(Constant.DBConstantString.USER_ID);
        String gameID = req.getParameter(Constant.DBConstantString.GAME_ID);
        String session = req.getParameter(Constant.DBConstantString.SESSION);

        LogLogin logLogin = new LogLogin(session,userID,gameID);
        
        try {
            ProducerLogLogin prod = new ProducerLogLogin(Constant.KafkaConstantString.TOPIC_LOG_LOGIN,LogLoginController.confifProducer);
            prod.sendLog(logLogin);
        } catch (ConfigException ex) {
            logger.error(ex.getMessage());
            return new ApiOutput(ApiOutput.STATUS_CODE.SYSTEM_ERROR.errorCode, "System happened error when set up");
        } catch(Exception ex){
            return new ApiOutput(ApiOutput.STATUS_CODE.REQUEST_TIME_OUT.errorCode, "Cannot push message to kafkaf");
        }
        return new ApiOutput(ApiOutput.STATUS_CODE.SUCCESS.errorCode, ApiOutput.STATUS_CODE.SUCCESS.msg);
    }
    
}
