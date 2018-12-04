/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.queue;

import exception.ConfigException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;

/**
 *
 * @author root
 */
public abstract class KafkaLogAbstract {
    
    /**
     *
     * @param hostAndPort
     * @param maxIdle
     * @param timeOut
     * @return true if have any topic exist and false if not
     * @throws ConfigException
     */
    public static boolean checkConnect(String hostAndPort, int maxIdle, int timeOut) throws ConfigException{
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hostAndPort);
        properties.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, maxIdle);
        properties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, timeOut);
        
        try (AdminClient client = KafkaAdminClient.create(properties))
        {
            ListTopicsResult topics = client.listTopics();
            Set<String> names = topics.names().get();
            if(names.isEmpty()){
                return false;
            }
            return true;
        }
        catch (InterruptedException | ExecutionException ex)
        {
            throw new ConfigException("Cannot run consumer with this config", ex);
        }
    }
}
