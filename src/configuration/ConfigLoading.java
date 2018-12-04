/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import exception.ConfigException;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;

/**
 *
 * @author root
 */
public class ConfigLoading {

    private static final Logger LOGGER = Logger.getLogger(ConfigLoading.class);
    
    protected IniPreferences iniPref;

    private static Map<String,ConfigLoading> holderConfigInstance;
    
    public static ConfigLoading getInstance(String pathFileConfig) throws ConfigException{
        
        if(holderConfigInstance == null){
            // Thread safe
            synchronized(ConfigLoading.class){
                if(holderConfigInstance == null){
                    
                    // Using hashtable for thread safe
                    holderConfigInstance = new Hashtable<>();
                }
            } 
        }
        
        if(!holderConfigInstance.containsKey(pathFileConfig)){
            ConfigLoading configLoading = new ConfigLoading(pathFileConfig);
            holderConfigInstance.put(pathFileConfig, configLoading);
        }
        
        return holderConfigInstance.get(pathFileConfig);
    }
    
    private ConfigLoading(String pathFile) throws ConfigException{
        try {
            Ini ini = new Ini(new File(pathFile));
            iniPref = new IniPreferences(ini);
        } catch (IOException ex) {
            throw new ConfigException("File not found",ex);
        }
    }
    
    
    public Preferences getNode(String node) throws ConfigException{
        try {
            if(!iniPref.nodeExists(node)){
                throw new ConfigException("Node "+node+" is not exist in this file");
            }
            return iniPref.node(node);
        } catch (BackingStoreException ex) {
            throw new ConfigException("Failure when loading node from file",ex);
        }
    }
    
}
