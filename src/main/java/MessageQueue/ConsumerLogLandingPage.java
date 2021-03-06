/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageQueue;

import Configuration.ConfigConsumer;
import Exception.ConfigException;
import Log.LogLandingPage;
import static MessageQueue.ConsumerLogAbstract.DEFAULT_CLIENT_ID;
import Serialization.LogLandingPageDeserializer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 *
 * @author root
 */
public class ConsumerLogLandingPage extends ConsumerLogAbstract<String,LogLandingPage>{
    
    public ConsumerLogLandingPage(List<String> topics, String clientID, String groupID, ConfigConsumer configConsumer) throws ConfigException {
        super(topics, clientID, groupID, LogLandingPageDeserializer.class.getName(),configConsumer);
    }
    
    public ConsumerLogLandingPage(String topic, String clientID, String groupID, ConfigConsumer configConsumer) throws ConfigException {
        this(Arrays.asList(topic), clientID, groupID, configConsumer);
    }

    public ConsumerLogLandingPage(String topic) throws ConfigException {
        this(topic,DEFAULT_CLIENT_ID,DEFAULT_GROUP_ID, new ConfigConsumer());
    }
    
    public ConsumerLogLandingPage(String topic, ConfigConsumer config) throws ConfigException{
        this(topic,DEFAULT_CLIENT_ID,DEFAULT_GROUP_ID, config);
    }

    @Override
    public List<LogLandingPage> poolLog(int timeoutInMillis) {
        ArrayList<ConsumerRecord<String, LogLandingPage>> listRecord= (ArrayList<ConsumerRecord<String, LogLandingPage>>) this.poll(timeoutInMillis);
        ArrayList<LogLandingPage> listLogLandingPage = new ArrayList<>();
        for(ConsumerRecord<String, LogLandingPage> record : listRecord){
            listLogLandingPage.add(record.value());
        }
        return listLogLandingPage;
    }
}
