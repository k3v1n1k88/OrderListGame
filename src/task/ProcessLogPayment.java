/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;

import configuration.ConfigConsumer;
import exception.ConfigException;
import object.log.LogPayment;
import message.queue.ConsumerLogPayment;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ProcessLogPayment extends ProcessLogAbstract{
    
    public ProcessLogPayment(ConfigConsumer config, int maximumThread) throws ConfigException{
        super(config,maximumThread);
        try {
            consumer = new ConsumerLogPayment(constant.KafkaConstantString.TOPIC_LOG_PAYMENT,config);
        } catch (ConfigException ex) {
            throw new ConfigException("Cannot create consumer", ex);
        }
    }
    
    public ProcessLogPayment(ConfigConsumer config) throws ConfigException{
        this(config, DEFAULT_MAXIMUM_THREAD);
    }
    
    @Override
    public void run() {
        while(true){
            excutor = Executors.newFixedThreadPool(maximumThread);
            try{
                ArrayList<LogPayment> list = (ArrayList<LogPayment>) consumer.poolLog(0);
                if (list.size()>0) {
                    for(LogPayment log:list){
//                        logger.info(log.parse2String());
                        excutor.submit(new WorkerLogPayment(log));
                    }
                }
    
            }catch(Exception ex){
                logger.error(ex.getMessage());
            }
        }
    }
    
}
