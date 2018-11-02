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
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ProcessLogLogin implements Runnable{
    
    private static final Logger logger = Logger.getLogger(ProcessLogLogin.class);
    private ConfigConsumer config;
    ConsumerLogLogin consumer;
    
    private static final int MAXIMUM_THREAD = 100;
    
    public ProcessLogLogin(ConfigConsumer config){
        this.config = config;
    }
    @Override
    public void run() {
        try{
            consumer = new ConsumerLogLogin(constant.KafkaConstantString.TOPIC_LOG_LOGIN,config);
        }
        catch(ConfigException ex){
            System.out.println("Error when create consumer"+ex.getMessage());
            System.exit(0);
        }
        while(true){
            try{
                ExecutorService excutor = Executors.newFixedThreadPool(MAXIMUM_THREAD);
                ArrayList<LogLogin> list = (ArrayList<LogLogin>) consumer.poolLog(0);
                if (list.size()>0) {
                    for(LogLogin log:list){
                        System.out.println(log.parse2String());
                        excutor.submit(new WorkerLogLogin(log));
                    }
                }
    
            }catch(Exception ex){
                logger.error(ex.getMessage());
            }
        }
    }
}
