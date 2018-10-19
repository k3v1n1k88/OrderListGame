/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageQueue;

import Object.Log;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 *
 * @author root
 * @param <T>: instance of Object.Log
 */
public class WrapperProducer<T extends Log> {
    
    private String topic;
    private String clientID;
    private KafkaProducer producer;
    private Properties config;

    public WrapperProducer(String topic, String clientID, String fileConfigPath) throws IOException {
        this.topic = topic;
        this.clientID = clientID;
        this.config = LocalProducerConfig.getInstance(fileConfigPath).getConfig();
        this.config.put(ProducerConfig.CLIENT_ID_CONFIG, clientID);
        this.config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        this.config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(this.config);
    }
    
    public WrapperProducer(String topic) throws IOException{
        this(topic,Constant.KafkaConstantString.DEFAULT_CLIENT_ID,Constant.PathConstantString.PATH_TO_PRODUCER_CONFIG_FILE);
    }
    
    public Future<RecordMetadata> send(String K, T obj){
        ProducerRecord record = this.makeProducerRecord(K,obj.parse2String());
        return this.producer.send(record);
    }
    
    public Future<RecordMetadata> send(T obj){
        return this.send(null,obj);
    }
       
    public Properties getConfig() {
        return config;
    }

    public String getTopic() {
        return topic;
    }

    public String getClientID() {
        return clientID;
    }

    public KafkaProducer getProducer() {
        return producer;
    }
    
    private ProducerRecord makeProducerRecord(String K, String V){
        return new ProducerRecord<>(this.topic,K,V);
    }
    
    private ProducerRecord makeProducerRecord(String V){
        return this.makeProducerRecord(null,V);
    }
    public void close(){
        if(this.producer != null)
            this.producer.close();
    }
    
    public void flush(){
        if(this.producer != null)
            this.producer.flush();
    }
    
}
