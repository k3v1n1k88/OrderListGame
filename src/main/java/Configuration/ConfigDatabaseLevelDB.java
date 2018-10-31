/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuration;

import Exception.ConfigException;
import org.apache.log4j.Logger;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DBComparator;

/**
 *
 * @author root
 */
public class ConfigDatabaseLevelDB extends ConfigurationAbstract {
    
    private static final Logger logger = Logger.getLogger(ConfigDatabaseLevelDB.class);
    
    private boolean createIfMissing ;
    private boolean errorIfExists ;
    private int writeBufferSize ;
    private int maxOpenFiles ;
    private int blockRestartInterval ;
    private int blockSize ;
    private long cacheSize ;
    private boolean compression;
    private boolean verifyChecksums ;
    private boolean paranoidChecks ;
    
    private Logger log = null;

    public ConfigDatabaseLevelDB(String pathFileConfig) throws ConfigException{
        super(pathFileConfig,Constant.DBConstantString.DATABASE_LEVELDB);
        
        this.createIfMissing = this.prefs.getBoolean(Constant.DBConstantString.CREATE_IF_MISSING_CONFIG, ConfigDatabaseLevelDB.DEFAULT_CREATE_IF_MISSING);
        this.errorIfExists = this.prefs.getBoolean(Constant.DBConstantString.ERROR_IF_EXISTS_CONFIG, ConfigDatabaseLevelDB.DEFAULT_ERROR_IF_EXISTS);
        this.writeBufferSize = this.prefs.getInt(Constant.DBConstantString.WRITE_BUFFER_SIZE_CONFIG, ConfigDatabaseLevelDB.DEFAULT_WRITE_BUFFER_SIZE);
        this.maxOpenFiles = this.prefs.getInt(Constant.DBConstantString.MAX_OPEN_FILES_CONFIG, ConfigDatabaseLevelDB.DEFAULT_MAX_OPEN_FILES);
        this.blockRestartInterval = this.prefs.getInt(Constant.DBConstantString.BLOCK_RESTART_INTERVAL_CONFIG, ConfigDatabaseLevelDB.DEFAULT_BLOCK_RESTART_INTERVAL);
        this.blockSize = this.prefs.getInt(Constant.DBConstantString.BLOCK_SIZE_CONFIG, ConfigDatabaseLevelDB.DEFAULT_BLOCK_SIZE);
        this.cacheSize = this.prefs.getLong(Constant.DBConstantString.CACHE_SIZE_CONFIG, ConfigDatabaseLevelDB.DEFAULT_CACHE_SIZE);
        this.compression= this.prefs.getBoolean(Constant.DBConstantString.COMPRESSION_CONFIG, ConfigDatabaseLevelDB.DEFAULT_COMPRESSION);
        this.verifyChecksums = this.prefs.getBoolean(Constant.DBConstantString.VERIFY_CHECKSUMS_CONFIG, ConfigDatabaseLevelDB.DEFAULT_VERIFY_CHECKSUMS);
        this.paranoidChecks = this.prefs.getBoolean(Constant.DBConstantString.PARANOID_CHECKS_CONFIG, ConfigDatabaseLevelDB.DEFAULT_PARANOID_CHECKS);
        
        System.out.println("Config of leveldb database:"
            + "\n" + Constant.DBConstantString.CREATE_IF_MISSING_CONFIG + ":" + this.createIfMissing
            + "\n" + Constant.DBConstantString.ERROR_IF_EXISTS_CONFIG + ":" + this.errorIfExists
            + "\n" + Constant.DBConstantString.WRITE_BUFFER_SIZE_CONFIG + ":" + this.writeBufferSize
            + "\n" + Constant.DBConstantString.MAX_OPEN_FILES_CONFIG + ":" + this.maxOpenFiles
            + "\n" + Constant.DBConstantString.BLOCK_RESTART_INTERVAL_CONFIG + ":" + this.blockRestartInterval
            + "\n" + Constant.DBConstantString.BLOCK_SIZE_CONFIG + ":" + this.blockSize
            + "\n" + Constant.DBConstantString.CACHE_SIZE_CONFIG + ":" + this.cacheSize
            + "\n" + Constant.DBConstantString.COMPRESSION_CONFIG + ":" + this.compression
            + "\n" + Constant.DBConstantString.VERIFY_CHECKSUMS_CONFIG + ":" + this.verifyChecksums
            + "\n" + Constant.DBConstantString.PARANOID_CHECKS_CONFIG + ":" + this.paranoidChecks
        );
    }
    
    public void setLogger(Logger logger){
        if(logger!=null){
            this.log = logger ;
        }
    }
    
    public static Logger getLogger() {
        return logger;
    }

    public boolean isCreateIfMissing() {
        return createIfMissing;
    }

    public boolean isErrorIfExists() {
        return errorIfExists;
    }

    public int getWriteBufferSize() {
        return writeBufferSize;
    }

    public int getMaxOpenFiles() {
        return maxOpenFiles;
    }

    public int getBlockRestartInterval() {
        return blockRestartInterval;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public boolean isCompression() {
        return compression;
    }

    public boolean isVerifyChecksums() {
        return verifyChecksums;
    }

    public boolean isParanoidChecks() {
        return paranoidChecks;
    }

    public Logger getLog() {
        return log;
    }
    
    
    private static final boolean DEFAULT_CREATE_IF_MISSING = true ;
    private static final boolean DEFAULT_ERROR_IF_EXISTS = false;
    private static final int DEFAULT_WRITE_BUFFER_SIZE = 4<<20;
    private static final int DEFAULT_MAX_OPEN_FILES = 10000;
    private static final int DEFAULT_BLOCK_RESTART_INTERVAL = 16;
    private static final int DEFAULT_BLOCK_SIZE = 4*1024;
    private static final long DEFAULT_CACHE_SIZE = 100 * 1048576;
    private static final boolean DEFAULT_COMPRESSION = false;
    private static final boolean DEFAULT_VERIFY_CHECKSUMS = false;
    private static final boolean DEFAULT_PARANOID_CHECKS = false;
}
