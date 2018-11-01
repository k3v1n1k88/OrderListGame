/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Task;

import Configuration.ConfigConsumer;
import Exception.ConfigException;
import Log.LogPayment;
import MessageQueue.ConsumerLogPayment;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ProcessLogPayment implements Runnable{
    
    private static final Logger logger = Logger.getLogger(ProcessLogPayment.class);
    private ConfigConsumer config;
    ConsumerLogPayment consumer;
    private static final int MAXIMUM_THREAD = 100;
    
    public ProcessLogPayment(ConfigConsumer config){
        this.config = config;
    }
    @Override
    public void run() {
        try{
            consumer = new ConsumerLogPayment(Constant.KafkaConstantString.TOPIC_LOG_PAYMENT,config);
        }
        catch(ConfigException ex){
            System.out.println("Error when create consumer"+ex.getMessage());
            System.exit(0);
        }
        while(true){
            try{
                ExecutorService excutor = Executors.newFixedThreadPool(MAXIMUM_THREAD);
                ArrayList<LogPayment> list = (ArrayList<LogPayment>) consumer.poolLog(0);
                if (list.size()>0) {
                    for(LogPayment log:list){
                        System.out.println(log.parse2String());
                        excutor.submit(new WorkerLogPayment(log));
                    }
                }
    
            }catch(Exception ex){
                logger.error(ex.getMessage());
            }
        }
    }
    
}
