/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuration;

import Exception.ConfigException;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;

/**
 *
 * @author root
 */
public abstract class ConfigurationAbstract {

    private static final long serialVersionUID = 001L;

    private static final Logger LOGGER = Logger.getLogger(ConfigurationAbstract.class);

    protected Preferences prefs;

    public ConfigurationAbstract(String pathFile, String node) throws ConfigException{
        try {
            Ini ini = new Ini(new File(pathFile));
            IniPreferences iniPref = new IniPreferences(ini);
            this.prefs = iniPref.node(node);
        } catch (IOException ex) {
            throw new ConfigException("Fine not found",ex);
        }
    }
    
    public Preferences getPref() {
        return prefs;
    }
}
