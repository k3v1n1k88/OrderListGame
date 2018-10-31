/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bussiness;

import Configuration.ConfigConsumer;
import Exception.ConfigException;
import Task.ProcessLandingPage;
import Task.ProcessLogLogin;
import Task.ProcessLogPayment;
import org.apache.log4j.Logger;


/**
 *
 * @author root
 */
public class ServiceDaemon {
    
    private static final Logger logger = Logger.getLogger(ServiceDaemon.class);
    
    public static void main(String[] args) {
        try{
            ListOrderGameServer listOrderGameServer = ListOrderGameServer.getInstance();
            ConfigConsumer conf = new ConfigConsumer();
            new Thread(listOrderGameServer).start();
            new Thread(new ProcessLogPayment(conf)).start();
            new Thread(new ProcessLandingPage(conf)).start();
            new Thread(new ProcessLogLogin(conf)).start();
        }catch(ConfigException cex){
            System.out.println("Cannot start server because have problem with config");
        }
        
    }
}
