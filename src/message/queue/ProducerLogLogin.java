/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.queue;

import configuration.ConfigFactory;
import configuration.ConfigProducer;
import exception.ConfigException;
import object.log.LogLogin;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 *
 * @author root
 */
public class ProducerLogLogin extends ProducerLogAbstract<String,LogLogin> {

    public ProducerLogLogin(String topic, String clientID, ConfigProducer configProducer) throws ConfigException {
        super(topic, clientID, serialization.LogLoginSerializer.class.getName(),configProducer);
    }

    public ProducerLogLogin(String topic) throws ConfigException {
        this(topic, ConfigFactory.getConfigProducer(constant.PathConstant.PATH_TO_PRODUCER_CONFIG_FILE));
    }
    
    public ProducerLogLogin(String topic, ConfigProducer config) throws ConfigException{
        super(topic,serialization.LogLoginSerializer.class.getName(), config);
    }

    @Override
    public Future<RecordMetadata> sendLog(LogLogin logLogin) {
        return this.send(logLogin);
    }

    @Override
    public Future<RecordMetadata> sendLog(String key, LogLogin logLogin) {
        return this.send(key, logLogin);
    }

    @Override
    public Future<RecordMetadata> sendLog(String key, LogLogin logLogin, Callback callback) {
        return this.send(key, logLogin, callback);
    }
    
}
