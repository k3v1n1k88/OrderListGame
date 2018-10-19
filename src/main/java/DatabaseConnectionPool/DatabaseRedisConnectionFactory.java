/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;

/**
 *
 * @author root
 */
public class DatabaseRedisConnectionFactory implements PooledObjectFactory<DatabaseRedisConnection> {

    private DatabaseInfo databaseInfo;
    private final int connectionTimeout;
    private final int soTimeout;
    private final int database;
    private final String clientName;
    private final boolean ssl;
    private final SSLSocketFactory sslSocketFactory;
    private final SSLParameters sslParameters;
    private final HostnameVerifier hostnameVerifier;
    
    private Jedis connection;
    
    public DatabaseRedisConnectionFactory(String host,int port){
        this(new DatabaseInfo(host,port));
    }
    
    public DatabaseRedisConnectionFactory(DatabaseInfo databaseInfo){
        this(databaseInfo,2000,2000,false,0,Constant.DBConstantString.DEFAULT_CLIENT_NAME,null,null,null);
    }
    
    public DatabaseRedisConnectionFactory(DatabaseInfo databaseInfo, int database){
        this(databaseInfo,2000,2000,false,database,Constant.DBConstantString.DEFAULT_CLIENT_NAME,null,null,null);
    }
    
    public DatabaseRedisConnectionFactory(DatabaseInfo databaseInfo,
                                    int connectionTimeout,
                                    int soTimeout,
                                    boolean ssl,
                                    int database,
                                    String clientName,
                                    SSLSocketFactory sslSocketFactory,
                                    SSLParameters sslParameters,
                                    HostnameVerifier hostnameVerifier){
        this.databaseInfo = databaseInfo;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.ssl = ssl;
        this.database = database;
        this.clientName = clientName;
        this.sslSocketFactory = sslSocketFactory;
        this.sslParameters = sslParameters;
        this.hostnameVerifier = hostnameVerifier;
    }

    @Override
    public PooledObject<DatabaseRedisConnection> makeObject() throws Exception {
        DatabaseRedisConnection dbcnn = new DatabaseRedisConnection(this.databaseInfo,
                    this.connectionTimeout,
                    this.soTimeout,
                    this.ssl,
                    this.database,
                    this.clientName,
                    this.sslSocketFactory,
                    this.sslParameters,
                    this.hostnameVerifier);
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
            String host= this.databaseInfo.getHostAddress();
            int port = this.databaseInfo.getPort();
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
