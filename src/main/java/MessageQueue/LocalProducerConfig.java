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
import org.ini4j.Profile.Section;

/**
 *
 * @author root
 */
public class LocalProducerConfig {
    
    private static final Logger LOGGER = Logger.getLogger(LocalProducerConfig.class);
            
    private static LocalProducerConfig instance = null;
    private static String pathFile = "";
    
    private Properties config;
    
    private LocalProducerConfig(String pathFile) throws IOException{
        try {
            Ini ini = new Ini(new FileReader(pathFile));
            Section section = ini.get(KafkaConstantString.PRODUCER_KAFKA);
            this.config = new Properties();
            for(String key: section.keySet()){
                String values = section.get(key);
                if(values != null){
                    this.config.put(key, section.get(key));
                }
            }
        } catch (IOException ex) {
            LOGGER.info("Expected a valid config of ProducerConfig but got a unvalid path file: \""+pathFile+"\"\n"+ex);
            throw ex;
        }
    }
    
    public static LocalProducerConfig getInstance(String pathFile) throws IOException{
        if(LocalProducerConfig.instance == null||!LocalProducerConfig.pathFile.equals(pathFile)){
            synchronized(LocalProducerConfig.class){
                LocalProducerConfig.instance = new LocalProducerConfig(pathFile);
                LocalProducerConfig.pathFile = pathFile;
            }
        }
        return LocalProducerConfig.instance;
    }
    
    public Properties getConfig(){
        return this.config;
    }
    
}
