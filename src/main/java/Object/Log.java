/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

/**
 *
 * @author root
 */
public interface Log {
    
    // Help producer Kafka can access with Log instance;
    public String parse2String();
    
    // Help Consumer Kafka can return a Log instance
    public Log parse2Log(String infoLog) throws Exception;
}
