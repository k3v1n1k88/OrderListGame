/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageQueue;

import Constant.KafkaConstantString;
import Object.Log;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 * @param <T>
 */
public class WrapperConsumer<T extends Log>{
    
    private static final Logger LOGGER = Logger.getLogger(WrapperConsumer.class);
    
    private List<String> topics;
    private String clientID;
    private KafkaConsumer consumer;
    private Properties config;
    private Class<? extends Log> classLog;

    public WrapperConsumer(List<String> topics, String clientID, String groupID, String fileConfigPath, Class<? extends Log> classLog) throws IOException {
        this.topics = topics;
        this.clientID = clientID;
        this.config = LocalConsumerConfig.getInstance(fileConfigPath).getConfig();
        this.classLog = classLog;
        this.config.put(ConsumerConfig.CLIENT_ID_CONFIG, clientID);
        this.config.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        this.config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        this.config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<>(this.config);
        consumer.subscribe(topics);
    }
    
    public WrapperConsumer(String topic, String clientID, String groupID, String fileConfigPath, Class<? extends Log> classLog) throws IOException {
        this(Arrays.asList(topic),clientID,groupID,fileConfigPath,classLog);
    }
    
    public WrapperConsumer(String topic, Class<? extends Log> classLog) throws IOException{
        this(topic,KafkaConstantString.DEFAULT_CLIENT_ID,KafkaConstantString.DEFAULT_GROUP_ID,Constant.PathConstantString.PATH_TO_CONSUMER_CONFIG_FILE,classLog);
    }
    
    public List<T> poll(int timeoutInMillis) throws Exception{
        try {
            ArrayList<T> list = new ArrayList<>();
            Constructor<? extends Log> constructor = classLog.getConstructor();
            
            T helperLog = (T) constructor.newInstance();
            
            ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofMillis(timeoutInMillis));
            for (ConsumerRecord<String,String> record : records) {
                T log = (T) helperLog.parse2Log(record.value());
                list.add(log);
            }
            return list;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOGGER.error("Cannot parse records from consumer to Log instance. Please check class constructor parameter");
            throw ex;
        }
    }
    
    public void close(){
        if(this.consumer != null)
            this.consumer.close();
    }
            
    public Properties getConfig() {
        return config;
    }

    public List<String> getTopic() {
        return topics;
    }

    public String getClientID() {
        return clientID;
    }
    
    public KafkaConsumer getConsumer(){
        return this.consumer;
    }
    
}
