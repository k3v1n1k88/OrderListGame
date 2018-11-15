/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import api.ApiOutput;
import api.ApiServlet;
import cache.CacheLandingPage;
import exception.ConfigException;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class RecommendationController extends ApiServlet{
    private static final long serialVersionUID = 6535839539762841383L;
    private static final Logger logger = Logger.getLogger(RecommendationController.class);
    
    private static CacheLandingPage cache = null;
    
    static {
        try {
            cache = CacheLandingPage.getInstance();
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
        
        try {
            listGame = cache.getList(session);
            
            ApiOutput apiOutput;
            if(listGame != null){
                apiOutput = new ApiOutput(ApiOutput.STATUS_CODE.SUCCESS.errorCode, ApiOutput.STATUS_CODE.SUCCESS.msg);
                apiOutput.setRecommadationList(listGame.getRecommandatioList());
            }
            else{
                apiOutput = new ApiOutput(ApiOutput.STATUS_CODE.BAD_REQUEST.errorCode,"Cannot get list game with this session");
            }
            return apiOutput;
        } catch (ExecutionException ex) {
            logger.error(ex);
            return new ApiOutput(ApiOutput.STATUS_CODE.SYSTEM_ERROR.errorCode, ex.getMessage());
        } catch(Exception ex){
            logger.error(ex);
            return new ApiOutput(ApiOutput.STATUS_CODE.REQUEST_TIME_OUT.errorCode, "Cannot push message to kafkaf");
        }
    }
}
