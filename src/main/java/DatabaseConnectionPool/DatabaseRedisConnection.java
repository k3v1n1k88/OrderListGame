/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

/**
 *
 * @author root
 */
public class DatabaseRedisConnection implements DatabaseConnection{
    
    private static final Logger LOGGER = Logger.getLogger(DatabaseRedisConnection.class);
    
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
    
    public DatabaseRedisConnection(String host,int port){
        this(new DatabaseInfo(host,port));
    }
    
    public DatabaseRedisConnection(DatabaseInfo databaseInfo){
        this(databaseInfo,2000,2000,false,0,"admin",null,null,null);
    }
    
    public DatabaseRedisConnection(DatabaseInfo databaseInfo, int database){
        this(databaseInfo,2000,2000,false,database,"admin",null,null,null);
    }
    
    public DatabaseRedisConnection(DatabaseInfo databaseInfo,
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
    public void createConnection() throws Exception {
        if (this.connection == null) {
            this.connection = new Jedis(this.databaseInfo.getHostAddress(),
                    this.databaseInfo.getPort(),
                    this.connectionTimeout,
                    this.soTimeout,
                    this.ssl,
                    this.sslSocketFactory,
                    this.sslParameters,
                    this.hostnameVerifier);
            try {
                this.connection.connect();
                if (this.databaseInfo.getPassword() != null) {
                    this.connection.auth(this.databaseInfo.getPassword());
                }
                if (this.database != 0) {
                    this.connection.select(database);
                }
                if (this.clientName != null) {
                    this.connection.clientSetname(clientName);
                }
            } catch (JedisException je) {
                this.connection.close();
                LOGGER.info("Fail when creation connection."+je);
                throw je;
            }
        }
    }

    @Override
    public void close() throws Exception {
        if(this.connection != null){
            this.connection.close();
        }
    }

    @Override
    public boolean ping() {
        return this.connection.ping().equals("PONG");
    }
    public Jedis getConnection(){
        return this.connection;
    }
    
}
