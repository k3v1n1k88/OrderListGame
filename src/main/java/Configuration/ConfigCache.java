/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuration;

import Exception.ConfigException;


/**
 *
 * @author root
 */
public class ConfigCache extends ConfigurationAbstract {
    
    private long refreshAfterWrite;
    private long expireAfterWrite;
    
    private long expireAfterAccess;
    
    private long maximumSize;
    private long maximumWeight;

    public ConfigCache(String pathFile) throws ConfigException {
        
        super(pathFile, Constant.CacheConstantString.CACHE_NODE);
        this.refreshAfterWrite = this.prefs.getLong(Constant.CacheConstantString.REFRESH_AFTER_WRITE, ConfigCache.DEFAULT_REFRESH_AFTER_WRITE);
        this.expireAfterAccess = this.prefs.getLong(Constant.CacheConstantString.EXPIRE_AFTER_ACCESS, ConfigCache.DEFAULT_EXPIRE_AFTER_ACCESS);
        this.expireAfterWrite = this.prefs.getLong(Constant.CacheConstantString.EXPIRE_AFTER_WRITE, ConfigCache.DEFAULT_EXPIRE_AFTER_WRITE);
        this.maximumSize = this.prefs.getLong(Constant.CacheConstantString.MAXIMUM_SIZE, ConfigCache.DEFAULT_MAXIMUM_SIZE);
        this.maximumWeight = this.prefs.getLong(Constant.CacheConstantString.MAXIMUM_WEIGHT, ConfigCache.DEFAULT_MAXIMUM_WEIGHT);
        
        System.out.println("Config of cache: "
                + "\n" + Constant.CacheConstantString.REFRESH_AFTER_WRITE + ":" + this.refreshAfterWrite
                + "\n" + Constant.CacheConstantString.EXPIRE_AFTER_ACCESS + ":" + this.expireAfterAccess
                + "\n" + Constant.CacheConstantString.EXPIRE_AFTER_WRITE + ":" + this.expireAfterWrite
                + "\n" + Constant.CacheConstantString.MAXIMUM_SIZE + ":" + this.maximumSize
                + "\n" + Constant.CacheConstantString.MAXIMUM_WEIGHT + ":" + this.maximumWeight
        );
    }
    
    public ConfigCache() throws ConfigException{
        this(Constant.PathConstant.PATH_TO_CACHE_CONFIG_FILE);
    }

    public long getRefreshAfterWrite() {
        return refreshAfterWrite;
    }

    public long getExpireAfterWrite() {
        return expireAfterWrite;
    }

    public long getExpireAfterAccess() {
        return expireAfterAccess;
    }

    public long getMaximumSize() {
        return maximumSize;
    }

    public long getMaximumWeight() {
        return maximumWeight;
    }
    
    
    
    public static final long DEFAULT_REFRESH_AFTER_WRITE = 100;

    public static final long DEFAULT_EXPIRE_AFTER_WRITE = 100;

    public static final long DEFAULT_EXPIRE_AFTER_ACCESS = 0;

    public static final long DEFAULT_MAXIMUM_SIZE = 4194304;

    public static final long DEFAULT_MAXIMUM_WEIGHT = 1024;
    
}
