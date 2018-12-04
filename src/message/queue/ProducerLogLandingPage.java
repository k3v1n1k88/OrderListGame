/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.queue;

import configuration.ConfigFactory;
import configuration.ConfigProducer;
import exception.ConfigException;
import object.log.LogLandingPage;
import serialization.LogLandingPageSerializer;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 *
 * @author root
 */
public class ProducerLogLandingPage extends ProducerLogAbstract<String,LogLandingPage> {
    
    public ProducerLogLandingPage(String topic, String clientID, ConfigProducer configProducer) throws ConfigException {
        super(topic, clientID, LogLandingPageSerializer.class.getName(), configProducer);
    }
    
    public ProducerLogLandingPage(String topic, ConfigProducer configProducer) throws ConfigException {
        super(topic, LogLandingPageSerializer.class.getName(), configProducer);
    }
    
    public ProducerLogLandingPage(String topic) throws ConfigException {
        this(topic, ConfigFactory.getConfigProducer(topic));
    }

    @Override
    public Future<RecordMetadata> sendLog(LogLandingPage log) {
        return this.send(log);
    }

    @Override
    public Future<RecordMetadata> sendLog(String key, LogLandingPage log) {
        return this.send(key, log);
    }

    @Override
    public Future<RecordMetadata> sendLog(String key, LogLandingPage log, Callback callback) {
        return this.send(key, log, callback);
    }
}
