///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package message.queue;
//
//import exception.ConfigException;
//import object.log.LogLogin;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import org.apache.kafka.clients.producer.RecordMetadata;
//
///**
// *
// * @author root
// */
//public class producer {
//
//    public static void main(String[] args) throws ConfigException, InterruptedException, ExecutionException {
//        ProducerLogLogin producer = new ProducerLogLogin("LogLogin");
//        //ProducerRecord record ;
////        KafkaProducer producer = wProducer.getProducer();
////        record = wProducer.makeProducerRecord("123");
////        producer.send(record);
////        LogPayment logLogin = new LogPayment("123","345","37483");
////        Future<RecordMetadata> f = producerLogPayment.sendLog(logLogin);
////        System.out.println(f.get());
//        Future<RecordMetadata> f = producer.send(new LogLogin("123","456","823"));
//        System.out.println(f.get());
//    }
//}
