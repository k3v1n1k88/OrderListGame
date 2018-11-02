/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constant;

/**
 *
 * @author root
 */
public class DBConstantString {

    public static final long serialVersionUID = 4548952039293333L;
    
    // For identify node config for redis database
    public static final String DATABASE_REDIS = "database_redis";
    
    // For identify node config for leveldb database
    public static final String DATABASE_LEVELDB = "database_leveldb";

    public static final String PASSWORD = "password";

    public static final String USERNAME = "username";

    public static final String HOST = "host";

    public static final String PORT = "port";

    public static final String DATABASE_SCORING_NAME = "database_scoring";

    public static final String DATABASE_RECOMMENDATION_SCORING_NAME = "database_recommendation";

    public static final String DATABASE_MISSING_MAPPING_NAME = "database_missing_mapping";

    public static final String DATABASE_MAPPING_NAME = "DatabaseMapping";

    public static final String SESSION = "session";

    public static final String GAME_ID = "gameID";

    public static final String USER_ID = "userID";

    public static final String AMOUNT = "amount";

    public static final String WEEK = "week";

    public static final String LOGIN_TIMES = "loginTimes";

    public static final String SCORE = "score";

    public static final String DATE = "date";

    public static final String CONNECTION_TIMEOUT = "connection_timeout";

    public static final String SOCKET_TIMEOUT = "socket_timeout";

    public static final String CLIENT_NAME = "client_name";
    
    public static final String SSL = "ssl";

    public static final String DATABASE_INDEX = "database_index";
    
    public static final String TIMESTAMP = "time";
    
    public static final String POINT = "point";
    
    public static final String LIST_GAME = "listgame";
    
    
    /***************************************************************************/
    // WARING: just ONLY use for REDIS determine database to select
    /***************************************************************************/
    public static final int DATABASE_SCORING_INDEX = 0;
    
    public static final int DATABASE_MAPPING_GAMEID_SESSION_INDEX = 1;
    
    public static final int DATABASE_RECOMMENDATION_INDEX = 2;
    /***************************************************************************/
    
    
    
    /***************************************************************************/
    // WARNING: just ONLY use for LEVELDB
    // Constant String config for levelDB
    /***************************************************************************/
    
    public static final String CREATE_IF_MISSING_CONFIG = "create_if_missing";

    public static final String ERROR_IF_EXISTS_CONFIG = "error_if_exists";

    public static final String WRITE_BUFFER_SIZE_CONFIG = "write_buffer_size";

    public static final String MAX_OPEN_FILES_CONFIG = "max_open_files";

    public static final String BLOCK_RESTART_INTERVAL_CONFIG = "block_restart_interval";

    public static final String BLOCK_SIZE_CONFIG = "block_size";

    public static final String CACHE_SIZE_CONFIG = "cache_size";

    public static final String COMPRESSION_CONFIG = "compression";

    public static final String VERIFY_CHECKSUMS_CONFIG = "verify_checksums";

    public static final String PARANOID_CHECKS_CONFIG = "paranoid_checks";
    /***************************************************************************/
    
    private DBConstantString(){}
    
}
