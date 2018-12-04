/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connection;

import configuration.ConfigDatabaseRedis;
import exception.ConfigException;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import redis.clients.jedis.Jedis;

/**
 *
 * @author root
 */
public class DatabaseRedisConnectionFactory implements PooledObjectFactory<DatabaseRedisConnection> {
    
    private ConfigDatabaseRedis config;
    private String clientName;
    private int database;
    
    private static final String DEFAULT_CLIENT_NAME = "vng.com.vn";
    private static final int DEFAULT_DATABASE = 0;
    
    public DatabaseRedisConnectionFactory(int database, String clientName) throws ConfigException{
        this(database,clientName,new ConfigDatabaseRedis());
    }
    
    public DatabaseRedisConnectionFactory(int database, String clientName, ConfigDatabaseRedis config) throws ConfigException{
        this.config = config;
        this.database = database;
        this.clientName = clientName;
    }
    
    public DatabaseRedisConnectionFactory(String clientName, ConfigDatabaseRedis config) throws ConfigException{
        this(DatabaseRedisConnectionFactory.DEFAULT_DATABASE,clientName,config);
    }
    
    public DatabaseRedisConnectionFactory(String clientName) throws ConfigException{
        this(DatabaseRedisConnectionFactory.DEFAULT_DATABASE, clientName);
    }
    
    public DatabaseRedisConnectionFactory() throws ConfigException{
        this(DatabaseRedisConnectionFactory.DEFAULT_CLIENT_NAME);
    }
    @Override
    public PooledObject<DatabaseRedisConnection> makeObject() throws Exception {
        DatabaseRedisConnection dbcnn = new DatabaseRedisConnection(this.config.getHost(),
                    this.config.getPort(),
                    this.config.getPassword(),
                    this.config.getConnectionTimeoutMillius(),
                    this.config.getSoTimeoutMillius(),
                    this.config.isSSL(),
                    this.database,
                    this.clientName);
        dbcnn.createConnection();
        return new DefaultPooledObject<DatabaseRedisConnection>(dbcnn);
    }

    @Override
    public void destroyObject(PooledObject<DatabaseRedisConnection> pooledJedis) throws Exception {
        DatabaseRedisConnection dbcnn = pooledJedis.getObject();
        Jedis jedis = dbcnn.getConnection();
        if (jedis.isConnected()) {
            try {
                try {
                    jedis.quit();
                } catch (Exception e) {
                }
                jedis.disconnect();
            } catch (Exception e) {

            }
        }
    }

    @Override
    public boolean validateObject(PooledObject<DatabaseRedisConnection> pooledJedis) {
        DatabaseRedisConnection dbcnn = pooledJedis.getObject();
        Jedis jedis = dbcnn.getConnection();
        try {
            String host= this.config.getHost();
            int port = this.config.getPort();
            String connectionHost = jedis.getClient().getHost();
            int connectionPort = jedis.getClient().getPort();

            return host.equals(connectionHost)
                    && port == connectionPort && jedis.isConnected()
                    && jedis.ping().equals("PONG");
            
        } catch (final Exception e) {
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<DatabaseRedisConnection> pooledJedis) throws Exception {
        DatabaseRedisConnection dbcnn = pooledJedis.getObject();
        Jedis jedis = dbcnn.getConnection();
        if (jedis.getDB() != this.database) {
            jedis.select(this.database);
        }
    }

    @Override
    public void passivateObject(PooledObject<DatabaseRedisConnection> pooledJedis) throws Exception {
        
    }
    
}
