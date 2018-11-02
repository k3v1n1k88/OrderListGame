/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.queue;

import configuration.ConfigProducer;
import exception.ConfigException;
import object.log.LogLandingPage;
import static message.queue.ProducerLogAbstract.DEFAULT_CLIENT_ID;
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

    public ProducerLogLandingPage(String topic) throws ConfigException {
        this(topic,DEFAULT_CLIENT_ID, new ConfigProducer());
    }
    
    public ProducerLogLandingPage(String topic, ConfigProducer config) throws ConfigException{
        this(topic,DEFAULT_CLIENT_ID, config);
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
