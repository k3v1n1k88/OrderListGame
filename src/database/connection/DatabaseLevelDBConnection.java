/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connection;

import configuration.ConfigDatabaseLevelDB;
import exception.DatabaseException;
import org.iq80.leveldb.*;
import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;
import org.apache.log4j.Logger;
import org.fusesource.leveldbjni.JniDBFactory;

/**
 *
 * @author root
 */
public class DatabaseLevelDBConnection implements DatabaseConnection{
    
    private static final Logger LOGGER = Logger.getLogger(DatabaseLevelDBConnection.class);
    
    private DatabaseInfo databaseInfo;
    
    private Options option;
    private DB connection = null;
    
    private org.iq80.leveldb.Logger logger;
    
    public DatabaseLevelDBConnection(DatabaseInfo databaseInfo,Options option){
        this.databaseInfo  = databaseInfo;
        this.option = option;
    }
    
    public DatabaseLevelDBConnection(DatabaseInfo databaseInfo){
        this(databaseInfo, new Options());
    }
    
    public DatabaseLevelDBConnection(String databasename){
        this(new DatabaseInfo(databasename));
    }
    
    public DatabaseLevelDBConnection(ConfigDatabaseLevelDB config){
        this(config.getDatabaseName(),
                config.isCreateIfMissing(),
                config.isErrorIfExists(),
                config.getWriteBufferSize(),
                config.getMaxOpenFiles(),
                config.getBlockRestartInterval(),
                config.getBlockSize(),
                config.getCacheSize(),
                config.isCompression(),
                config.isVerifyChecksums(),
                config.isParanoidChecks(),
                config.getLog());
    }
    
    public DatabaseLevelDBConnection(String databaseName,
                                boolean createIfMissing,
                                boolean errorIfExists,
                                int writeBufferSize,
                                int maxOpenFiles,
                                int blockRestartInterval,
                                int blockSize,
                                long cacheSize,
                                boolean compression,
                                boolean verifyChecksums,
                                boolean paranoidChecks,
                                org.iq80.leveldb.Logger logger){
        
        this(new DatabaseInfo(databaseName),
                createIfMissing,
                errorIfExists,
                writeBufferSize,
                maxOpenFiles,
                blockRestartInterval,
                blockSize,
                cacheSize,
                compression,
                verifyChecksums,
                paranoidChecks,
                logger);
    }
    
    public DatabaseLevelDBConnection(String databaseName,
                                boolean createIfMissing,
                                boolean errorIfExists,
                                int writeBufferSize,
                                int maxOpenFiles,
                                int blockRestartInterval,
                                int blockSize,
                                long cacheSize,
                                boolean compression,
                                boolean verifyChecksums,
                                boolean paranoidChecks){
        
        this(databaseName,
                createIfMissing,
                errorIfExists,
                writeBufferSize,
                maxOpenFiles,
                blockRestartInterval,
                blockSize,
                cacheSize,
                compression,
                verifyChecksums,
                paranoidChecks,
                null);
    }
    
    
    public DatabaseLevelDBConnection(DatabaseInfo databaseInfo,
                                boolean createIfMissing,
                                boolean errorIfExists,
                                int writeBufferSize,
                                int maxOpenFiles,
                                int blockRestartInterval,
                                int blockSize,
                                long cacheSize,
                                boolean compression,
                                boolean verifyChecksums,
                                boolean paranoidChecks,
                                org.iq80.leveldb.Logger logger){
        this.databaseInfo = databaseInfo;
        this.option = new Options();
        this.option.createIfMissing(createIfMissing);
        this.option.errorIfExists(errorIfExists);
        this.option.writeBufferSize(writeBufferSize);
        this.option.maxOpenFiles(maxOpenFiles);
        this.option.blockSize(blockSize);
        this.option.blockRestartInterval(blockRestartInterval);
        if(cacheSize != -1)
            this.option.cacheSize(cacheSize);
        if(compression == true)
            this.option.compressionType(CompressionType.SNAPPY);
        else
            this.option.compressionType(CompressionType.NONE);
        this.option.verifyChecksums(verifyChecksums);
        this.option.paranoidChecks(paranoidChecks);
        if(logger != null){
            this.option.logger(logger);
        }
    }
    
    @Override
    public void createConnection() throws DatabaseException {
        DBFactory factory = new JniDBFactory();
        try {
            connection = factory.open(new File(this.databaseInfo.getDatabaseName()), this.option);
        } catch (IOException ex) {
            throw new DatabaseException("Cannot open file",ex);
        }
    }

    @Override
    public void close() throws DatabaseException {
        if(this.connection != null){
            try{
                this.connection.close();
            }catch(IOException ex){
                throw new DatabaseException("Cannot close database",ex);
            }
        }
    }
    
    public DB getConnection() throws DatabaseException{
        return this.connection;
    }

    @Override
    public boolean ping() {
        try{
            this.connection.put(bytes("ping"), bytes("pong"));
            String res = asString(this.connection.get(bytes("ping")));
            this.connection.delete(bytes("ping"));
            return ("pong".equals(res));
        }catch(DBException e){
            return false;
        }
    }
}
