/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;

import configuration.ConfigConsumer;
import exception.ConfigException;
import object.log.LogLandingPage;
import message.queue.ConsumerLogLandingPage;
import message.queue.ConsumerLogPayment;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ProcessLandingPage implements Runnable{
    
    private static final Logger logger = Logger.getLogger(ProcessLogPayment.class);
    private ConfigConsumer config;
    ConsumerLogLandingPage consumer;
    
    private static final int MAXIMUM_THREAD = 100;
    
    public ProcessLandingPage(ConfigConsumer config){
        this.config = config;
    }
    @Override
    public void run() {
        try{
            consumer = new ConsumerLogLandingPage(constant.KafkaConstantString.TOPIC_LANDING_PAGE,config);
        }
        catch(ConfigException ex){
            System.out.println("Error when create consumer"+ex.getMessage());
            System.exit(0);
        }
        ExecutorService excutor = Executors.newFixedThreadPool(MAXIMUM_THREAD);
        while(true){
            try{
                ArrayList<LogLandingPage> list = (ArrayList<LogLandingPage>) consumer.poolLog(0);
                if (list.size()>0) {
                    for(LogLandingPage log:list){
                        System.out.println(log.parse2String());
                        excutor.submit(new WorkerLogLandingPage(log));
                    }
                }
    
            }catch(Exception ex){
                logger.error(ex.getMessage());
            }
        }
    }
}
