/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialization;

import object.log.LogPayment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class LogPaymentSerializer implements Serializer<LogPayment> {
    
    private static final Logger logger = Logger.getLogger(LogPaymentSerializer.class);
    
    @Override
    public void configure(Map<String, ?> map, boolean bln) {
        //NOTHING TO DO
    }

    @Override
    public byte[] serialize(String arg0, LogPayment arg1) {
        byte[] returnVal = null;
        ObjectMapper objMapper = new ObjectMapper();
        try {
            returnVal = objMapper.writeValueAsString(arg1).getBytes();
        } catch (JsonProcessingException ex) {
            logger.error("Cannot serialize", ex);
        }
        return returnVal;
    }

    @Override
    public void close() {
        //NO THING TO DO
    }
}
