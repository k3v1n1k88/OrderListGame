/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialization;

import object.log.LogLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class LogLoginDeserializer implements Deserializer<LogLogin>{
    
    private static final Logger logger = Logger.getLogger(LogLoginDeserializer.class);
    
    @Override
    public void configure(Map<String, ?> map, boolean bln) {
         //NOTHING TO DO
    }

    @Override
    public LogLogin deserialize(String arg0, byte[] arg1) {
        ObjectMapper objMapper = new ObjectMapper();
        LogLogin logLogin = null;
        
        try {
            logLogin = objMapper.readValue(arg1, LogLogin.class);
        } catch (IOException ex) {
            logger.error("Cannot desrialize",ex);
        }
        
        return logLogin;
    }

    @Override
    public void close() {
        //NOTHING TO DO
    }
    
}
