/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.queue;

import configuration.ConfigFactory;
import configuration.ConfigProducer;
import exception.ConfigException;
import object.log.LogPayment;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
/**
 *
 * @author root
 */
public class ProducerLogPayment extends ProducerLogAbstract<String,LogPayment>{
    
    public ProducerLogPayment(String topic, String clientID, ConfigProducer configProducer) throws ConfigException {
        super(topic, clientID, serialization.LogPaymentSerializer.class.getName(),configProducer);
    }

    public ProducerLogPayment(String topic) throws ConfigException {
        this(topic, ConfigFactory.getConfigProducer(constant.PathConstant.PATH_TO_PRODUCER_CONFIG_FILE));
    }
    
    public ProducerLogPayment(String topic, ConfigProducer configProducer) throws ConfigException {
        super(topic, serialization.LogPaymentSerializer.class.getName(), configProducer);
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
