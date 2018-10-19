/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

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
    
    public DatabaseLevelDBConnection(DatabaseInfo databaseInfo,Options option){
        this.databaseInfo  = databaseInfo;
        this.option = option;
    }
    @Override
    public void createConnection() throws IOException {
        DBFactory factory = new JniDBFactory();
        try {
            connection = factory.open(new File(this.databaseInfo.getDatabaseName()), this.option);
        } catch (IOException ex) {
            LOGGER.error("Create connection failure with database name: "+this.databaseInfo.getDatabaseName());
            throw ex;
        }
    }

    @Override
    public void close() throws Exception {
        if(this.connection != null){
            this.connection.close();
        }
    }
    
    public DB getConnection() throws IOException{
        return this.connection;
    }

    @Override
    public boolean ping() {
        return this.connection != null;
    }
}
