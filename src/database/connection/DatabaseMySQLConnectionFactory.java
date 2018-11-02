/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connection;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 *
 * @author root
 */
public class DatabaseMySQLConnectionFactory implements PooledObjectFactory<DatabaseMySQLConnection>{
    
    private DatabaseInfo databaseInfo;
    
    public DatabaseMySQLConnectionFactory(String hostAddress, int port, String databaseName, String userName, String password){
        this(new DatabaseInfo(hostAddress, port, databaseName, userName, password));
    }
    
    public DatabaseMySQLConnectionFactory(DatabaseInfo databaseInfo){
        this.databaseInfo = databaseInfo;
    }   

    @Override
    public PooledObject<DatabaseMySQLConnection> makeObject() throws Exception {
        DatabaseMySQLConnection dbcnn = new DatabaseMySQLConnection(this.databaseInfo);
        dbcnn.createConnection();
        return new DefaultPooledObject<DatabaseMySQLConnection>(dbcnn);
    }

    @Override
    public void destroyObject(PooledObject<DatabaseMySQLConnection> pooledMySQL) throws Exception {
        DatabaseMySQLConnection dbcnn = pooledMySQL.getObject();
        dbcnn.close();
    }

    @Override
    public boolean validateObject(PooledObject<DatabaseMySQLConnection> pooledMySQL) {
        DatabaseMySQLConnection dbcnn = pooledMySQL.getObject();
        return dbcnn.ping();
        
    }

    @Override
    public void activateObject(PooledObject<DatabaseMySQLConnection> pooledMySQL) throws Exception {
        
    }

    @Override
    public void passivateObject(PooledObject<DatabaseMySQLConnection> pooledMySQL) throws Exception {
        
    }
    
    
}
