/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.iq80.leveldb.Options;

/**
 *
 * @author root
 */
public class DatabaseLevelDBConnectionFactory implements PooledObjectFactory<DatabaseLevelDBConnection>{

    public DatabaseInfo databaseInfo;
    public Options option;
    
    public DatabaseLevelDBConnectionFactory(String databaseName){
        this(new DatabaseInfo(databaseName));
    }
    
    public DatabaseLevelDBConnectionFactory(DatabaseInfo databaseInfo){
        this(databaseInfo,new Options().createIfMissing(true));
    }
    
    public DatabaseLevelDBConnectionFactory(DatabaseInfo databaseInfo,Options option){
        this.databaseInfo = databaseInfo;
        this.option = option;
    }

    public DatabaseInfo getDatabaseInfo() {
        return this.databaseInfo;
    }

    public Options getOption() {
        return this.option;
    } 

    @Override
    public PooledObject<DatabaseLevelDBConnection> makeObject() throws Exception {
        DatabaseLevelDBConnection dbcnn = new DatabaseLevelDBConnection(this.databaseInfo,this.option);
        dbcnn.createConnection();
        return new DefaultPooledObject<DatabaseLevelDBConnection>(dbcnn);
    }

    @Override
    public void destroyObject(PooledObject<DatabaseLevelDBConnection> pooledMySQL) throws Exception {
        DatabaseLevelDBConnection dbcnn = pooledMySQL.getObject();
        dbcnn.close();
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
