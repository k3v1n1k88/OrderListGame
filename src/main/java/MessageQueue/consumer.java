/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageQueue;

import Exception.ParseLogException;
import Object.LogLogin;
import Object.LogPayment;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;

/**
 *
 * @author root
 */
public class consumer {

    public static void main(String[] args) throws IOException, ParseException, ParseLogException {
        Jedis jedis = new Jedis("localhost", 6379,2000,2000,true);
        jedis.connect();
        jedis.auth("nhakhoahoc");
        System.out.println(jedis.ping());
//        WrapperConsumer wConsumer = new WrapperConsumer("123",LogPayment.class);
////        KafkaConsumer consumer = wConsumer.getConsumer();
//        System.out.println("Consumer starting...");
//        while (true) {
//            try {
//                List<LogPayment> listPayment = wConsumer.poll(1000);
//                for(LogPayment log : listPayment){
//                    System.out.println(log.parse2String());
//                }
//            } catch (Exception ex) {
//                
//            }
//        }
    }
}
