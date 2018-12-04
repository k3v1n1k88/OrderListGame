/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.queue;

import configuration.ConfigConsumer;
import exception.ConfigException;
import object.log.Log;
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
 * @param <K> : key
 * @param <V> : message
 */
public abstract class ConsumerLogAbstract<K extends Object,V extends Log> extends KafkaLogAbstract {
    
    private static final Logger LOGGER = Logger.getLogger(ConsumerLogAbstract.class);
    
    protected static final String DEFAULT_CLIENT_ID = "vng.com.vn";
    protected static final String DEFAULT_GROUP_ID = "vng.com.vn";
    
    private List<String> topics;
    private String clientID;
    private String groupID;
    
    private ConfigConsumer config;
    private KafkaConsumer<K,V> consumer;

    public ConsumerLogAbstract(List<String> topics, String clientID, String groupID, String pathFileDeserializer, ConfigConsumer configConsumer) throws ConfigException {
        this.topics = topics;
        this.clientID = clientID;
        this.groupID = groupID;
        
        if(configConsumer == null){
            throw new ConfigException("Get null config");
        }
        
        this.config = configConsumer;
        
        Properties props = new Properties();
        
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientID);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, pathFileDeserializer);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.config.getAutoOffsetReset());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.config.getBootstrapServers());
        props.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, this.config.getConnectionsMaxIdleMs());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.config.getEnableAutoCommit());
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, this.config.getFetchMaxBytes());
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, this.config.getFetchMaxWaitMs());
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, this.config.getFetchMinBytes());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, this.config.getMaxPartitionFetchBytes());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, this.config.getMaxPollIntervalMs());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.config.getMaxPollRecords());
        props.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, this.config.getReceiveBufferBytes());
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, this.config.getRequestTimeoutMs());
        props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, this.config.getRetryBackoffMs());
        props.put(ConsumerConfig.SEND_BUFFER_CONFIG, this.config.getSendBufferBytes());
        
        this.consumer = new KafkaConsumer<>(props);
        
        consumer.subscribe(topics);
    }
    
    public ConsumerLogAbstract(String topic, String clientID, String groupID, String pathFileSerialize, ConfigConsumer configConsumer) throws ConfigException {
        this(Arrays.asList(topic),clientID,groupID, pathFileSerialize, configConsumer);
    }
    
    public ConsumerLogAbstract(String topic, String clientID, String groupID, String pathFileSerialize) throws ConfigException {
        this(Arrays.asList(topic),clientID,groupID, pathFileSerialize, new ConfigConsumer());
    }
    
    public ConsumerLogAbstract(String topic, String pathFileSerialize, ConfigConsumer configConsumer) throws ConfigException{
        this(Arrays.asList(topic),DEFAULT_CLIENT_ID,DEFAULT_GROUP_ID, pathFileSerialize, configConsumer);
    }
    
    public ConsumerLogAbstract(String topic, String pathFileSerialize) throws ConfigException{
        this(Arrays.asList(topic),DEFAULT_CLIENT_ID,DEFAULT_GROUP_ID, pathFileSerialize, new ConfigConsumer());
    }
    
    protected List<ConsumerRecord<K, V>> poll(int timeoutInMillis){
        ArrayList<ConsumerRecord<K, V>> list = new ArrayList<>();
        ConsumerRecords<K, V> records = this.consumer.poll(timeoutInMillis);
        for (ConsumerRecord<K, V> record : records) {
            list.add(record);
        }
        return list;
    }
    
    public abstract List<V> poolLog(int timeoutInMillis);
    
    public void close(){
        if(this.consumer != null)
            this.consumer.close();
    }

    public List<String> getTopics() {
        return topics;
    }

    public String getClientID() {
        return clientID;
    }

    public String getGroupID() {
        return groupID;
    }

    public ConfigConsumer getConfig() {
        return config;
    }

}
