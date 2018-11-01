/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageQueue;

import Configuration.ConfigProducer;
import Exception.ConfigException;
import java.util.Properties;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;
/**
 *
 * @author root
 */
public abstract class ProducerLogAbstract<K extends Object, V extends Object> {
    
    private static final Logger logger = Logger.getLogger(ProducerLogAbstract.class);
    
    protected String topic;
    protected String clientID;
    
    private KafkaProducer<K,V> producer;
    protected ConfigProducer config;

    protected static final String DEFAULT_CLIENT_ID = "vng.com.vn";
    
    public ProducerLogAbstract(String topic, String clientID, String pathFileSerializer, ConfigProducer configProducer) throws ConfigException{
        
        if(topic == null||pathFileSerializer == null||configProducer == null){
            throw new ConfigException("Error happended when load config to create producer");
        }
            
        this.config = configProducer;
        this.topic = topic;
        this.clientID = clientID;
        
        Properties props = new Properties();
        
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientID);
        
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, pathFileSerializer);
        
        props.put(ProducerConfig.ACKS_CONFIG, this.config.getAcks());

        props.put(ProducerConfig.BATCH_SIZE_CONFIG, this.config.getBatchSize());

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.config.getBootstrapServers());

        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, this.config.getBufferMemory());

        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, this.config.getCompressionType());

        props.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, this.config.getConnectionsMaxIdleMs());

        props.put(ProducerConfig.LINGER_MS_CONFIG, this.config.getLingerMs());

        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, this.config.getMaxBlockMs());

        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, this.config.getMaxRequestSize());

        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, this.config.getRequestTimeoutMs());

        props.put(ProducerConfig.RETRIES_CONFIG, this.config.getRetries());

        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, this.config.getRetryBackoffMs());

        props.put(ProducerConfig.SEND_BUFFER_CONFIG, this.config.getSendBufferBytes());
        
        producer = new KafkaProducer<>(props);
       
    }
    
    public ProducerLogAbstract(String topic, String pathFileSerializer, ConfigProducer configProducer ) throws ConfigException{
        this(topic,DEFAULT_CLIENT_ID, pathFileSerializer, configProducer);
        
    }
    
    public ProducerLogAbstract(String topic, String pathFileSerializer) throws ConfigException{
        this(topic,DEFAULT_CLIENT_ID,pathFileSerializer, new ConfigProducer());
    }
    
    
    protected Future<RecordMetadata> send(K key, V value,Callback callback){
        ProducerRecord<K,V> record = this.makeProducerRecord(key,value);
        return this.producer.send(record,callback);
    }
    
    protected Future<RecordMetadata> send(K key, V value){
        return this.send(key,value,null);
    }
    
    protected Future<RecordMetadata> send(V value){
        return this.send(null,value);
    }
    
    private ProducerRecord<K,V> makeProducerRecord(K key, V value){
        return new ProducerRecord<>(this.topic,key,value);
    }
    
    private ProducerRecord<K,V> makeProducerRecord(V value){
        return this.makeProducerRecord(null,value);
    }
    
    public void close(){
        if(this.producer != null)
            this.producer.close();
    }
    
    public void flush(){
        if(this.producer != null)
            this.producer.flush();
    }

    public String getTopic() {
        return topic;
    }

    public String getClientID() {
        return clientID;
    }

    public ConfigProducer getConfig() {
        return config;
    }
    
    public abstract Future<RecordMetadata> sendLog(V value);
    public abstract Future<RecordMetadata> sendLog(K key, V value);
    public abstract Future<RecordMetadata> sendLog(K key, V value, Callback callback);
    
}
