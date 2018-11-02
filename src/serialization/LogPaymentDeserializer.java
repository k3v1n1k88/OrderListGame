/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialization;

import object.log.LogPayment;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class LogPaymentDeserializer implements Deserializer<LogPayment> {
        
    private static final Logger logger = Logger.getLogger(LogPaymentDeserializer.class);
    
    @Override
    public void configure(Map<String, ?> map, boolean bln) {
         //NOTHING TO DO
    }

    @Override
    public LogPayment deserialize(String arg0, byte[] arg1) {
        ObjectMapper objMapper = new ObjectMapper();
        LogPayment logPayment = null;
        
        try {
            logPayment = objMapper.readValue(arg1, LogPayment.class);
        } catch (IOException ex) {
            logger.error("Cannot desrialize",ex);
        }
        
        return logPayment;
    }

    @Override
    public void close() {
        //NOTHING TO DO
    }
    
}
