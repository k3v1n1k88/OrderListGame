/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageQueue;

import Configuration.ConfigConsumer;
import Exception.ConfigException;
import Log.LogPayment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 *
 * @author root
 */
public class ConsumerLogPayment extends ConsumerLogAbstract<String,LogPayment> {
    
    public ConsumerLogPayment(List<String> topics, String clientID, String groupID, ConfigConsumer configConsumer) throws ConfigException {
        super(topics, clientID, groupID, Serialization.LogPaymentDeserializer.class.getName(),configConsumer);
    }
    
    public ConsumerLogPayment(String topic, String clientID, String groupID, ConfigConsumer configConsumer) throws ConfigException {
        this(Arrays.asList(topic), clientID, groupID, configConsumer);
    }

    public ConsumerLogPayment(String topic, ConfigConsumer config) throws ConfigException  {
        this(topic,DEFAULT_CLIENT_ID,DEFAULT_GROUP_ID, config);
    }
    
    @Override
    public List<LogPayment> poolLog(int timeoutInMillis) {
        ArrayList<ConsumerRecord<String, LogPayment>> listRecord= (ArrayList<ConsumerRecord<String, LogPayment>>) this.poll(timeoutInMillis);
        ArrayList<LogPayment> listPayment = new ArrayList<>();
        for(ConsumerRecord<String, LogPayment> record : listRecord){
            listPayment.add(record.value());
        }
        return listPayment;
    }
}
