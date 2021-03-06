/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuration;

import Exception.ConfigException;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class ConfigOfSystem extends ConfigurationAbstract{
    
    private static final Logger LOGGER = Logger.getLogger(ConfigOfSystem.class);
    
    /*  To config unit time for day, week or seccond
        week => 604800 (secs)
        day => 86400 (secs)
        hours => 3600 (secs)
        minute => 60 (secs)
    */
    private long unitTime;
    
    /* Unit payment use for indentify point for payment times.
        Ex: 10000 => 1 point
    */
    private long unitPayment;

    // Point per login time
    private int pointPerLogin;
    
    private double baseOfPower;
    
    private int limitScoringGame;
    
    private int limitRecommendationGame;
    
    
    public ConfigOfSystem(String path) throws ConfigException{
        super(path,Constant.SystemConstantString.SYSTEM);
        
        this.unitTime = this.prefs.getLong(Constant.SystemConstantString.UNIT_TIME, ConfigOfSystem.DEFAULT_UNIT_TIME);
        this.unitPayment = this.prefs.getLong(Constant.SystemConstantString.UNIT_PAYMENT, ConfigOfSystem.DEFAULT_UNIT_PAYMENT);
        this.pointPerLogin = this.prefs.getInt(Constant.SystemConstantString.POINT_PER_LOGIN, ConfigOfSystem.DEFAULT_POINT_PER_LOGIN);
        this.baseOfPower = this.prefs.getDouble(Constant.SystemConstantString.BASE_OF_POWER, ConfigOfSystem.DEFAULT_BASE_OF_POWER);
        this.limitRecommendationGame = this.prefs.getInt(Constant.SystemConstantString.LIMIT_RECOMMENDATION_GAME, ConfigOfSystem.DEFAULT_LIMIT_RECOMMENDATION_GAME);
        this.limitScoringGame =  this.prefs.getInt(Constant.SystemConstantString.LIMIT_SCORING_GAME, ConfigOfSystem.DEFAULT_LIMIT_SCORING_GAME);
        
        System.out.println("Config of system with params:"
                + "\n" + Constant.SystemConstantString.UNIT_TIME + ":" + this.unitTime
                + "\n" + Constant.SystemConstantString.UNIT_PAYMENT + ":" + this.unitPayment
                + "\n" + Constant.SystemConstantString.POINT_PER_LOGIN + ":" + this.pointPerLogin
                + "\n" + Constant.SystemConstantString.BASE_OF_POWER + ":" + this.baseOfPower
                + "\n" + Constant.SystemConstantString.LIMIT_RECOMMENDATION_GAME + ":" + this.limitRecommendationGame
                + "\n" + Constant.SystemConstantString.LIMIT_SCORING_GAME + ":" + this.limitScoringGame
        );
    }

    public ConfigOfSystem() throws ConfigException {
        this(Constant.PathConstant.PATH_TO_SYSTEM_CONFIG_FILE);
    }

    public long getUnitTime() {
        return unitTime;
    }

    public long getUnitPayment() {
        return unitPayment;
    }

    public int getPointPerLogin() {
        return pointPerLogin;
    }

    public double getBaseOfPower() {
        return baseOfPower;
    }
    
    private static final long DEFAULT_UNIT_TIME = 604800;
    
    private static final long DEFAULT_UNIT_PAYMENT = 10000;

    private static final int DEFAULT_POINT_PER_LOGIN = 1;
    
    private static float DEFAULT_BASE_OF_POWER = 2;
    
    private static final int DEFAULT_LIMIT_RECOMMENDATION_GAME = 5;
    
    private static final int DEFAULT_LIMIT_SCORING_GAME = 5;
}
