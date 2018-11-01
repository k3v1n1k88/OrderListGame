/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import Configuration.ConfigDatabaseLevelDB;
import Exception.ConfigException;
import Exception.DatabaseException;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 *
 * @author root
 */
public class DatabaseLevelDBConnectionFactory implements PooledObjectFactory<DatabaseLevelDBConnection>{

    private String databaseName;
    
    private ConfigDatabaseLevelDB config;
    
    public DatabaseLevelDBConnectionFactory(String databaseName, String pathDatabaseConfig) throws ConfigException{
        config = new ConfigDatabaseLevelDB(pathDatabaseConfig);
        this.databaseName = databaseName;
    }
    
    public DatabaseLevelDBConnectionFactory(String databaseName) throws ConfigException{
        this(databaseName, Constant.PathConstant.PATH_TO_DATABASE_LEVELDB_CONFIG_FILE);
    }
    
    
    public String getDatabaseName() {
        return databaseName;
    }

    public ConfigDatabaseLevelDB getConfig() {
        return config;
    }
    
    

    @Override
    public PooledObject<DatabaseLevelDBConnection> makeObject() throws DatabaseException {
        DatabaseLevelDBConnection dbcnn = new DatabaseLevelDBConnection(this.databaseName,
                                                        this.config.isCreateIfMissing(),
                                                        this.config.isErrorIfExists(),
                                                        this.config.getWriteBufferSize(),
                                                        this.config.getMaxOpenFiles(),
                                                        this.config.getBlockRestartInterval(),
                                                        this.config.getBlockSize(),
                                                        this.config.getCacheSize(),
                                                        this.config.isCompression(),
                                                        this.config.isVerifyChecksums(),
                                                        this.config.isParanoidChecks()
                                                        );
        dbcnn.createConnection();
        return new DefaultPooledObject<DatabaseLevelDBConnection>(dbcnn);
    }

    @Override
    public void destroyObject(PooledObject<DatabaseLevelDBConnection> pooledMySQL) throws Exception {
        DatabaseLevelDBConnection dbcnn = pooledMySQL.getObject();
        if(dbcnn!= null){
            dbcnn.close();
        }
    }
    @Override
    public boolean validateObject(PooledObject<DatabaseLevelDBConnection> pooledMySQL) {
        DatabaseLevelDBConnection dbcnn = pooledMySQL.getObject();
        return dbcnn.ping();
    }

    @Override
    public void activateObject(PooledObject<DatabaseLevelDBConnection> pooledMySQL) throws Exception {
        
    }

    @Override
    public void passivateObject(PooledObject<DatabaseLevelDBConnection> pooledMySQL) throws Exception {
        
    }

    
}
