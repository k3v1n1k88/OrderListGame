/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageQueue;

import Constant.KafkaConstantString;
import Constant.PoolConstantString;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.ini4j.Profile;

/**
 *
 * @author root
 */
public class LocalConsumerConfig {
    private static final Logger LOGGER = Logger.getLogger(LocalConsumerConfig.class);
            
    private static LocalConsumerConfig instance = null;
    private static String pathFile = "";
    
    private Properties config;
    
    private LocalConsumerConfig(String pathFile) throws IOException{
        try {
            Ini ini = new Ini(new FileReader(pathFile));
            Profile.Section section = ini.get(KafkaConstantString.CONSUMER_KAFKA);
            this.config = new Properties();
            for(String key: section.keySet()){
                String values = section.get(key);
                if(values != null){
                    this.config.put(key, section.get(key));
                }
            }
        } catch (IOException ex) {
            LOGGER.info("Expected a valid config of ConsumerConfig but got a unvalid path file: \""+pathFile+"\"\n"+ex);
            throw ex;
        }
    }
    
    public static LocalConsumerConfig getInstance(String pathFile) throws IOException{
        if(LocalConsumerConfig.instance == null||!LocalConsumerConfig.pathFile.equals(pathFile)){
            synchronized(LocalConsumerConfig.class){
                LocalConsumerConfig.instance = new LocalConsumerConfig(pathFile);
                LocalConsumerConfig.pathFile = pathFile;
            }
        }
        return LocalConsumerConfig.instance;
    }
    
    public Properties getConfig(){
        return this.config;
    }
}
