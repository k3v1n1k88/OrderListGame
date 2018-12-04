/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;

import configuration.ConfigConsumer;
import exception.ConfigException;
import object.log.LogLogin;
import message.queue.ConsumerLogLogin;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import message.queue.ConsumerLogLandingPage;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ProcessLogLogin extends ProcessLogAbstract{

    public ProcessLogLogin(ConfigConsumer config, int maximumThread) throws ConfigException{
        super(config, maximumThread);
        try {
            consumer = new ConsumerLogLogin(constant.KafkaConstantString.TOPIC_LANDING_PAGE, configConsumer);
        } catch (ConfigException ex) {
            throw new ConfigException("Cannot create consumer",ex);
        }
    }

    public ProcessLogLogin(ConfigConsumer config) throws ConfigException {
        this(config, DEFAULT_MAXIMUM_THREAD);
    }
    
    @Override
    public void run() {
        excutor = Executors.newFixedThreadPool(maximumThread);
        while(true){
            try{
                
                ArrayList<LogLogin> list = (ArrayList<LogLogin>) consumer.poolLog(0);
                if (list.size()>0) {
                    for(LogLogin log:list){
//                        logger.info(log.parse2String());
                        excutor.submit(new WorkerLogLogin(log));
                    }
                }
    
            }catch(Exception ex){
                logger.error(ex.getMessage(),ex);
            }
        }
    }
}
