/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.queue;

import configuration.ConfigConsumer;
import exception.ConfigException;
import object.log.LogLogin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 *
 * @author root
 */
public class ConsumerLogLogin extends ConsumerLogAbstract<String,LogLogin>  {

    public ConsumerLogLogin(List<String> topics, String clientID, String groupID, ConfigConsumer configConsumer) throws ConfigException {
        super(topics, clientID, groupID, serialization.LogLoginDeserializer.class.getName(),configConsumer);
    }
    
    public ConsumerLogLogin(String topic, String clientID, String groupID, ConfigConsumer configConsumer) throws ConfigException {
        this(Arrays.asList(topic), clientID, groupID, configConsumer);
    }

    public ConsumerLogLogin(String topic) throws ConfigException {
        this(topic,DEFAULT_CLIENT_ID,DEFAULT_GROUP_ID, new ConfigConsumer());
    }
    
    public ConsumerLogLogin(String topic, ConfigConsumer config) throws ConfigException{
        this(topic,DEFAULT_CLIENT_ID,DEFAULT_GROUP_ID, config);
    }
    
    @Override
    public List<LogLogin> poolLog(int timeoutInMillis) {
        ArrayList<ConsumerRecord<String, LogLogin>> listRecord= (ArrayList<ConsumerRecord<String, LogLogin>>) this.poll(timeoutInMillis);
        ArrayList<LogLogin> listLogLogin = new ArrayList<>();
        for(ConsumerRecord<String, LogLogin> record : listRecord){
            listLogLogin.add(record.value());
        }
        return listLogLogin;
    }
    
}
