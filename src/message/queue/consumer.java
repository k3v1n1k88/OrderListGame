/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.queue;

import exception.ConfigException;
import exception.ParseLogException;
import object.log.LogLogin;
import object.log.LogPayment;
import object.log.LogPayment;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import org.json.simple.parser.ParseException;

/**
 *
 * @author root
 */
public class consumer {

    public static void main(String[] args) throws IOException, ParseException, ParseLogException, ConfigException {
//        Jedis jedis = new Jedis("localhost", 6379,2000,2000,false);
//        jedis.connect();
//        jedis.auth("nhakhoahoc");
//        System.out.println(jedis.ping());
        ConsumerLogLogin consumer = new ConsumerLogLogin("LogLogin");
//        KafkaConsumer consumer = wConsumer.getConsumer();
        System.out.println("Consumer starting...");
        while (true) {
            try {
                ArrayList<LogLogin> listLog = (ArrayList<LogLogin>) consumer.poolLog(1000);
                for(LogLogin log : listLog){
                    System.out.println(log.parse2String());
                }
            } catch (Exception ex) {
                
            }
        }
    }
}
