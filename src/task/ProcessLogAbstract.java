/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task;

import configuration.ConfigConsumer;
import java.util.concurrent.ExecutorService;
import message.queue.ConsumerLogAbstract;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
    public abstract class ProcessLogAbstract implements Runnable{
    
    protected static final Logger logger = Logger.getLogger(ProcessLogAbstract.class);
    protected static final int DEFAULT_MAXIMUM_THREAD = 100;
    
    
    protected final ConfigConsumer configConsumer;
    protected ConsumerLogAbstract consumer;
    
    protected int maximumThread = 100;
    protected ExecutorService excutor;
    
    public ProcessLogAbstract(ConfigConsumer config, int maximumThread){
        this.configConsumer = config;
        this.maximumThread = maximumThread;
    }
    
}
