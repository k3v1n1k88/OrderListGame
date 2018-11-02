/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import exception.ConfigException;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ConfigProducer extends ConfigurationAbstract{
    
    private static final Logger LOGGER = Logger.getLogger(ConfigProducer.class);
    
    private String acks;

    private String batchSize;

    private String bootstrapServers;

    private String bufferMemory;

    private String compressionType;

    private String connectionsMaxIdleMs;

    private String lingerMs;

    private String maxBlockMs;

    private String maxRequestSize;

    private String requestTimeoutMs;

    private String retries;

    private String retryBackoffMs;

    private String sendBufferBytes;
    
    public ConfigProducer(String pathFile) throws ConfigException{
        super(pathFile,constant.PathConstant.PATH_TO_PRODUCER_CONFIG_FILE);
        
        this.acks = this.prefs.get(constant.KafkaConstantString.ACKS, DEFAULT_ACKS);
        this.batchSize = this.prefs.get(constant.KafkaConstantString.BATCH_SIZE, DEFAULT_BATCH_SIZE);
        this.bootstrapServers = this.prefs.get(constant.KafkaConstantString.BOOTSTRAP_SERVERS, DEFAULT_BOOTSTRAP_SERVERS);
        this.bufferMemory = this.prefs.get(constant.KafkaConstantString.BUFFER_MEMORY, DEFAULT_BUFFER_MEMORY);
        this.compressionType = this.prefs.get(constant.KafkaConstantString.COMPRESSION_TYPE, DEFAULT_COMPRESSION_TYPE);
        this.connectionsMaxIdleMs = this.prefs.get(constant.KafkaConstantString.CONNECTIONS_MAX_IDLE_MS, DEFAULT_CONNECTIONS_MAX_IDLE_MS);
        this.lingerMs = this.prefs.get(constant.KafkaConstantString.LINGER_MS, DEFAULT_LINGER_MS);
        this.maxBlockMs = this.prefs.get(constant.KafkaConstantString.MAX_BLOCK_MS, DEFAULT_MAX_BLOCK_MS);
        this.maxRequestSize = this.prefs.get(constant.KafkaConstantString.MAX_REQUEST_SIZE, DEFAULT_MAX_REQUEST_SIZE);
        this.requestTimeoutMs = this.prefs.get(constant.KafkaConstantString.REQUEST_TIMEOUT_MS, DEFAULT_REQUEST_TIMEOUT_MS);
        this.retries = this.prefs.get(constant.KafkaConstantString.RETRIES, DEFAULT_RETRIES);
        this.retryBackoffMs = this.prefs.get(constant.KafkaConstantString.RETRY_BACKOFF_MS, DEFAULT_RETRY_BACKOFF_MS);
        this.sendBufferBytes = this.prefs.get(constant.KafkaConstantString.SEND_BUFFER_BYTES, DEFAULT_SEND_BUFFER_BYTES);
    
        System.out.println("Load config producer with param: "
                + "\n" + ProducerConfig.ACKS_CONFIG + " : " + this.acks
                + "\n" + ProducerConfig.BATCH_SIZE_CONFIG + " : " + this.batchSize
                + "\n" + ProducerConfig.BOOTSTRAP_SERVERS_CONFIG + " : " + this.bootstrapServers
                + "\n" + ProducerConfig.BUFFER_MEMORY_CONFIG + " : " + this.bufferMemory
                + "\n" + ProducerConfig.COMPRESSION_TYPE_CONFIG + " : " + this.compressionType
                + "\n" + ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG + " : " + this.connectionsMaxIdleMs
                + "\n" + ProducerConfig.LINGER_MS_CONFIG + " : " + this.lingerMs
                + "\n" + ProducerConfig.MAX_BLOCK_MS_CONFIG + " : " + this.maxBlockMs
                + "\n" + ProducerConfig.MAX_REQUEST_SIZE_CONFIG + " : " + this.maxRequestSize
                + "\n" + ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG + " : " + this.requestTimeoutMs
                + "\n" + ProducerConfig.RETRIES_CONFIG + " : " + this.retries
                + "\n" + ProducerConfig.RETRY_BACKOFF_MS_CONFIG + " : " + this.retryBackoffMs
                + "\n" + ProducerConfig.SEND_BUFFER_CONFIG + " : " + this.sendBufferBytes
        );
    }
    
    public ConfigProducer() throws ConfigException{
        this(constant.PathConstant.PATH_TO_PRODUCER_CONFIG_FILE);
    }

    public String getAcks() {
        return acks;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getBufferMemory() {
        return bufferMemory;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public String getConnectionsMaxIdleMs() {
        return connectionsMaxIdleMs;
    }

    public String getLingerMs() {
        return lingerMs;
    }

    public String getMaxBlockMs() {
        return maxBlockMs;
    }

    public String getMaxRequestSize() {
        return maxRequestSize;
    }

    public String getRequestTimeoutMs() {
        return requestTimeoutMs;
    }

    public String getRetries() {
        return retries;
    }

    public String getRetryBackoffMs() {
        return retryBackoffMs;
    }

    public String getSendBufferBytes() {
        return sendBufferBytes;
    }
    
    
    private static final String DEFAULT_ACKS = "all";

    private static final String DEFAULT_BATCH_SIZE = "16384";

    private static final String DEFAULT_BOOTSTRAP_SERVERS = "localhost:9092";

    private static final String DEFAULT_BUFFER_MEMORY = "33554432";

    private static final String DEFAULT_COMPRESSION_TYPE = "none";

    private static final String DEFAULT_CONNECTIONS_MAX_IDLE_MS = "600000";

    private static final String DEFAULT_LINGER_MS = "0";

    private static final String DEFAULT_MAX_BLOCK_MS = "60000";

    private static final String DEFAULT_MAX_REQUEST_SIZE = "1048576";

    private static final String DEFAULT_REQUEST_TIMEOUT_MS = "30000";

    private static final String DEFAULT_RETRIES = "0";

    private static final String DEFAULT_RETRY_BACKOFF_MS = "100";

    private static final String DEFAULT_SEND_BUFFER_BYTES = "-1";

}
