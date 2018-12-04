/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import exception.ConfigException;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public abstract class ConfigAbstract {
    
    private static final Logger logger = Logger.getLogger(ConfigAbstract.class);
    
    protected Preferences prefs;
    
    public ConfigAbstract(String pathFile, String node) throws ConfigException{
        try {
            this.prefs = ConfigLoading.getInstance(pathFile).getNode(node);
        } catch (ConfigException ex) {
            logger.error(ex);
            throw ex;
        }
    }
    
    public abstract void showConfig();
    public abstract void checkConfig() throws ConfigException;
}
