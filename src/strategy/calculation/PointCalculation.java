/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy.calculation;

import configuration.ConfigSystem;
import exception.CalculationException;
import exception.ConfigException;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public abstract class PointCalculation {
    
    protected static final Logger logger = Logger.getLogger(PointCalculation.class);
    
    static ConfigSystem configOfSystem;
    
    protected long currentPoint;
    protected long latestLoginTime;
    protected long currentTime;
    
    static{
        try {
            configOfSystem = new ConfigSystem();
        } catch (ConfigException ex) {
            logger.error("Error when create config of system");
            System.exit(0);
        }
    }

    public PointCalculation(long currentPoint, long lastesLoginTime, long currentLoginTime) {
        this.currentPoint = currentPoint;
        this.latestLoginTime = lastesLoginTime;
        this.currentTime = currentLoginTime;
    }
    
    public abstract long calculatePoint() throws CalculationException;
}
