/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import api.ApiOutput;
import api.ApiServlet;
import cache.CacheLandingPage;
import configuration.ConfigProducer;
import exception.ConfigException;
import object.log.LogLandingPage;
import message.queue.ProducerLogLandingPage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class LandingPageController extends ApiServlet{

    private static final long serialVersionUID = 6535839539762841383L;
    private static final Logger logger = Logger.getLogger(LandingPageController.class);
    
    private static ConfigProducer configProducer = null;
    
    private static CacheLandingPage cache = null;
    
    static {
        try {
            configProducer = new ConfigProducer(constant.PathConstant.PATH_TO_PRODUCER_CONFIG_FILE);
            cache = new CacheLandingPage();
        } catch (ConfigException ex) {
            logger.info(ex.getMessage());
            logger.error(ex);
            System.exit(0);
        }
    }
    
    @Override
    protected ApiOutput execute(HttpServletRequest req, HttpServletResponse resp) {
        if (!checkValidParam(req, new String[]{ constant.DBConstantString.SESSION})) {
            
            return new ApiOutput(ApiOutput.STATUS_CODE.BAD_REQUEST.errorCode, "Parameter is not valid");
            
        }
        CacheLandingPage.ListGame listGame = null;
        String session = req.getParameter(constant.DBConstantString.SESSION);
        LogLandingPage logLandingPage = new LogLandingPage(session);
        
        try {
            ProducerLogLandingPage prod = new ProducerLogLandingPage(constant.KafkaConstantString.TOPIC_LANDING_PAGE,LandingPageController.configProducer);
            prod.sendLog(logLandingPage);
            listGame = cache.getList(session);
        } catch (ConfigException ex) {
            logger.error(ex.getMessage());
            return new ApiOutput(ApiOutput.STATUS_CODE.SYSTEM_ERROR.errorCode, "System happened error when setting up");
        } catch(Exception ex){
            logger.error(ex.getMessage());
            return new ApiOutput(ApiOutput.STATUS_CODE.REQUEST_TIME_OUT.errorCode, "Cannot push message to kafkaf");
        }
        
        ApiOutput apiOutput;
        if(listGame != null){
            apiOutput = new ApiOutput(ApiOutput.STATUS_CODE.SUCCESS.errorCode, ApiOutput.STATUS_CODE.SUCCESS.msg);
            apiOutput.setRecommadationList(listGame.getRecommandatioList());
            apiOutput.setScoringList(listGame.getScoringList());
        }
        else{
            apiOutput = new ApiOutput(ApiOutput.STATUS_CODE.BAD_REQUEST.errorCode,"Cannot get list game with this session");
        }
        return apiOutput;
    }
    
}
