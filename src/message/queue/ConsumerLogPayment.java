/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.queue;

import configuration.ConfigConsumer;
import exception.ConfigException;
import object.log.LogPayment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 *
 * @author root
 */
public class ConsumerLogPayment extends ConsumerLogAbstract<String,LogPayment> {
    
    private static final String CLIENT_ID = "Log Payment";
    private static final String GROUP_ID = "Log Payment";
    
    public ConsumerLogPayment(List<String> topics, String clientID, String groupID, ConfigConsumer configConsumer) throws ConfigException {
        super(topics, clientID, groupID, serialization.LogPaymentDeserializer.class.getName(),configConsumer);
    }
    
    public ConsumerLogPayment(String topic, String clientID, String groupID, ConfigConsumer configConsumer) throws ConfigException {
        this(Arrays.asList(topic), clientID, groupID, configConsumer);
    }

    public ConsumerLogPayment(String topic, ConfigConsumer config) throws ConfigException  {
        this(topic, CLIENT_ID, GROUP_ID, config);
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
