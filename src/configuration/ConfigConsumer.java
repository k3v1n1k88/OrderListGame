/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import exception.ConfigException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import message.queue.KafkaLogAbstract;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ConfigConsumer extends ConfigAbstract{
    
    private static final Logger LOGGER = Logger.getLogger(ConfigConsumer.class);
            
    private String autoOffsetReset = "latest";

    private String bootstrapServers = "localhost:9092";

    private String connectionsMaxIdleMs = "600000";

    private String enableAutoCommit = "true";

    private String fetchMaxBytes = "52428800";

    private String fetchMaxWaitMs = "500";

    private String fetchMinBytes = "1"; 

    private String maxPartitionFetchBytes = "1048576";

    private String maxPollIntervalMs = "300000";

    private String maxPollRecords = "500";

    private String receiveBufferBytes = "-1";

    private String requestTimeoutMs = "40000";

    private String retryBackoffMs  = "100";

    private String sendBufferBytes = "-1";
    
    public ConfigConsumer() throws ConfigException{
        this(constant.PathConstant.PATH_TO_CONSUMER_CONFIG_FILE);
    }
    
    public ConfigConsumer(String pathFile) throws ConfigException{
        
        super(pathFile, constant.KafkaConstantString.CONSUMER_KAFKA_NODE);
        
        this.autoOffsetReset = this.prefs.get(constant.KafkaConstantString.AUTO_OFFSET_RESET,ConfigConsumer.DEFAULT_AUTO_OFFSET_RESET);

        this.bootstrapServers = this.prefs.get(constant.KafkaConstantString.BOOTSTRAP_SERVERS,ConfigConsumer.DEFAULT_BOOTSTRAP_SERVERS);

        this.connectionsMaxIdleMs = this.prefs.get(constant.KafkaConstantString.CONNECTIONS_MAX_IDLE_MS,ConfigConsumer.DEFAULT_CONNECTIONS_MAX_IDLE_MS);

        this.enableAutoCommit = this.prefs.get(constant.KafkaConstantString.ENABLE_AUTO_COMMIT,ConfigConsumer.DEFAULT_ENABLE_AUTO_COMMIT);

        this.fetchMaxBytes = this.prefs.get(constant.KafkaConstantString.FETCH_MAX_BYTES,ConfigConsumer.DEFAULT_FETCH_MAX_BYTES);

        this.fetchMaxWaitMs = this.prefs.get(constant.KafkaConstantString.FETCH_MAX_WAIT_MS,ConfigConsumer.DEFAULT_FETCH_MAX_WAIT_MS);

        this.fetchMinBytes = this.prefs.get(constant.KafkaConstantString.FETCH_MIN_BYTES,ConfigConsumer.DEFAULT_FETCH_MIN_BYTES); 

        this.maxPartitionFetchBytes = this.prefs.get(constant.KafkaConstantString.MAX_PARTITION_FETCH_BYTES,ConfigConsumer.DEFAULT_MAX_PARTITION_FETCH_BYTES);

        this.maxPollIntervalMs = this.prefs.get(constant.KafkaConstantString.MAX_POLL_INTERVAL_MS,ConfigConsumer.DEFAULT_MAX_POLL_INTERVAL_MS);

        this.maxPollRecords = this.prefs.get(constant.KafkaConstantString.MAX_POLL_RECORDS,ConfigConsumer.DEFAULT_MAX_POLL_RECORDS);

        this.receiveBufferBytes = this.prefs.get(constant.KafkaConstantString.RECEIVE_BUFFER_BYTES,ConfigConsumer.DEFAULT_RECEIVE_BUFFER_BYTES);

        this.requestTimeoutMs = this.prefs.get(constant.KafkaConstantString.REQUEST_TIMEOUT_MS,ConfigConsumer.DEFAULT_REQUEST_TIMEOUT_MS);

        this.retryBackoffMs  = this.prefs.get(constant.KafkaConstantString.RETRY_BACKOFF_MS,ConfigConsumer.DEFAULT_RETRY_BACKOFF_MS);

        this.sendBufferBytes = this.prefs.get(constant.KafkaConstantString.SEND_BUFFER_BYTES,ConfigConsumer.DEFAULT_SEND_BUFFER_BYTES);
        
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getConnectionsMaxIdleMs() {
        return connectionsMaxIdleMs;
    }

    public String getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public String getFetchMaxBytes() {
        return fetchMaxBytes;
    }

    public String getFetchMaxWaitMs() {
        return fetchMaxWaitMs;
    }

    public String getFetchMinBytes() {
        return fetchMinBytes;
    }

    public String getMaxPartitionFetchBytes() {
        return maxPartitionFetchBytes;
    }

    public String getMaxPollIntervalMs() {
        return maxPollIntervalMs;
    }

    public String getMaxPollRecords() {
        return maxPollRecords;
    }

    public String getReceiveBufferBytes() {
        return receiveBufferBytes;
    }

    public String getRequestTimeoutMs() {
        return requestTimeoutMs;
    }

    public String getRetryBackoffMs() {
        return retryBackoffMs;
    }

    public String getSendBufferBytes() {
        return sendBufferBytes;
    }
    
    private static String DEFAULT_AUTO_OFFSET_RESET = "latest";

    private static String DEFAULT_BOOTSTRAP_SERVERS = "localhost:9092";

    private static String DEFAULT_CONNECTIONS_MAX_IDLE_MS = "600000";

    private static String DEFAULT_ENABLE_AUTO_COMMIT = "true";

    private static String DEFAULT_FETCH_MAX_BYTES = "52428800";

    private static String DEFAULT_FETCH_MAX_WAIT_MS = "500";

    private static String DEFAULT_FETCH_MIN_BYTES = "1"; 

    private static String DEFAULT_MAX_PARTITION_FETCH_BYTES = "1048576";

    private static String DEFAULT_MAX_POLL_INTERVAL_MS = "300000";

    private static String DEFAULT_MAX_POLL_RECORDS = "500";

    private static String DEFAULT_RECEIVE_BUFFER_BYTES = "-1";

    private static String DEFAULT_REQUEST_TIMEOUT_MS = "40000";

    private static String DEFAULT_RETRY_BACKOFF_MS  = "100";

    private static String DEFAULT_SEND_BUFFER_BYTES = "-1";

    @Override
    public void showConfig() {  
        
        LOGGER.info("Create consumer with params:"
                + "\n" + ConsumerConfig.AUTO_OFFSET_RESET_CONFIG + " : " +  this.autoOffsetReset
                + "\n" + ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG + " : " +  this.bootstrapServers
                + "\n" + ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG + " : " +  this.connectionsMaxIdleMs
                + "\n" + ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG + " : " +  this.enableAutoCommit
                + "\n" + ConsumerConfig.FETCH_MAX_BYTES_CONFIG + " : " +  this.fetchMaxBytes
                + "\n" + ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG + " : " +  this.fetchMaxWaitMs
                + "\n" + ConsumerConfig.FETCH_MIN_BYTES_CONFIG + " : " +  this.fetchMinBytes
                + "\n" + ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG + " : " +  this.maxPartitionFetchBytes
                + "\n" + ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG + " : " +  this.maxPollIntervalMs
                + "\n" + ConsumerConfig.MAX_POLL_RECORDS_CONFIG + " : " +  this.maxPollRecords
                + "\n" + ConsumerConfig.RECEIVE_BUFFER_CONFIG + " : " +  this.receiveBufferBytes
                + "\n" + ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG + " : " +  this.requestTimeoutMs
                + "\n" + ConsumerConfig.RETRY_BACKOFF_MS_CONFIG + " : " +  this.retryBackoffMs
                + "\n" + ConsumerConfig.SEND_BUFFER_CONFIG + " : " +  this.sendBufferBytes
        );
    }
    @Override
    public void checkConfig() throws ConfigException {
        boolean check = KafkaLogAbstract.checkConnect(this.bootstrapServers, 10000, 5000);
        if (check == false) {
            throw new ConfigException("Cannot find topic with this config");
        }
    }
}
