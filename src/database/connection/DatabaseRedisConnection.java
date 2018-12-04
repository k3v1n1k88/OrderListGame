/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Sharded;

/**
 *
 * @author root
 */
public class DatabaseRedisConnection implements DatabaseConnection{
    
    private static final Logger LOGGER = Logger.getLogger(DatabaseRedisConnection.class);
    
    private DatabaseInfo databaseInfo;
    
    private int database;
    private boolean ssl;
    private int connectionTimeout;
    private int soTimeout;
    private String clientName;
    
    SSLSocketFactory sslSocketFactory;
    SSLParameters sslParameters;
    HostnameVerifier hostnameVerifier;
    
    private Jedis connection;
    
    public DatabaseRedisConnection(String host,int port){
        this(new DatabaseInfo(host,port));
    }
    
    public DatabaseRedisConnection(DatabaseInfo databaseInfo){
        this(databaseInfo,2000,2000,false,0,"admin");
    }
    
    public DatabaseRedisConnection(DatabaseInfo databaseInfo, int database){
        this(databaseInfo,2000,2000,false,database,"admin");
    }
    
    public DatabaseRedisConnection(DatabaseInfo databaseInfo,
                                    int connectionTimeout,
                                    int soTimeout,
                                    boolean ssl,
                                    int database,
                                    String clientName){
            
        this(databaseInfo,connectionTimeout,soTimeout,ssl,database,clientName,null,null,null);
        
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
    }
    
    public DatabaseRedisConnection(String host, 
                                    int port, 
                                    String password, 
                                    int connectionTimeout, 
                                    int soTimeout, 
                                    boolean ssl,
                                    int database,
                                    String clientName,
                                    SSLSocketFactory sslSocketFactory, 
                                    SSLParameters sslParameters, 
                                    HostnameVerifier hostnameVerifier
                                    ){
        this(new DatabaseInfo(host,port,String.valueOf(database),clientName,password),
                connectionTimeout,
                soTimeout,
                ssl,
                database,
                clientName,
                sslSocketFactory,
                sslParameters,
                hostnameVerifier);
    }
    
    public DatabaseRedisConnection(String host, 
                                    int port, 
                                    String password, 
                                    int connectionTimeout, 
                                    int soTimeout, 
                                    boolean ssl,
                                    int database,
                                    String clientName
                                    ){
        this(host,port,password,connectionTimeout,soTimeout,ssl,database,clientName,null,null,null);
    }


    @Override
    public void createConnection() throws JedisException {
        if (this.connection == null) {
            
            JedisShardInfo info = new JedisShardInfo(this.databaseInfo.getHostAddress(),
                                                    this.databaseInfo.getPort(),
                                                    this.connectionTimeout,
                                                    this.soTimeout,
                                                    Sharded.DEFAULT_WEIGHT);
            this.connection = new Jedis(info);
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
    public void close() throws JedisException {
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
    
    public void selectDatabase(int database){
        this.connection.select(database);
    }
}
