/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageQueue;

import Configuration.ConfigProducer;
import Exception.ConfigException;
import Log.LogPayment;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
/**
 *
 * @author root
 */
public class ProducerLogPayment extends ProducerLogAbstract<String,LogPayment>{
    
    public ProducerLogPayment(String topic, String clientID, ConfigProducer configProducer) throws ConfigException {
        super(topic, clientID, Serialization.LogPaymentSerializer.class.getName(),configProducer);
    }

    public ProducerLogPayment(String topic) throws ConfigException {
        this(topic,DEFAULT_CLIENT_ID,new ConfigProducer());
    }
    
    public ProducerLogPayment(String topic, ConfigProducer configProducer) throws ConfigException {
        this(topic,DEFAULT_CLIENT_ID,configProducer);
    }

    @Override
    public Future<RecordMetadata> sendLog(LogPayment logLogin) {
        return this.send(logLogin);
    }

    @Override
    public Future<RecordMetadata> sendLog(String key, LogPayment logLogin) {
        return this.send(key, logLogin);
    }

    @Override
    public Future<RecordMetadata> sendLog(String key, LogPayment logLogin, Callback callback) {
        return this.send(key, logLogin, callback);
    }
}
