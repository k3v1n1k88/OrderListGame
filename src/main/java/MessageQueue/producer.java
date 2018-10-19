/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageQueue;

import Object.LogLogin;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 *
 * @author root
 */
public class producer {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        WrapperProducer<LogLogin> wProducer = new WrapperProducer<>("123", WrapperProducer.class.toString(), Constant.PathConstantString.PATH_TO_CONSUMER_CONFIG_FILE);
        //ProducerRecord record ;
//        KafkaProducer producer = wProducer.getProducer();
//        record = wProducer.makeProducerRecord("123");
//        producer.send(record);
        Future<RecordMetadata> f = wProducer.send(new LogLogin("123","345","37483"));
        System.out.println(f.get());
    }
}
