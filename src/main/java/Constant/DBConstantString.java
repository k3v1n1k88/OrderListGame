/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constant;

/**
 *
 * @author root
 */
public class DBConstantString {

    private static final long serialVersionUID = 4548952039293333L;
    
//    //config of database server
//    public static final int PORT = 7777;
    public static final String DEFAULT_PASSWORD = "admin";
    public static final String DEFAULT_USERNAME = "root";
//    public static final String HOST_ADDRESS = "localhost";
    public static final String DEFAULT_CLIENT_NAME = "admin";
    
    //name of databases
    public static final String DATABASE_SCORING_NAME = "database_scoring";
    public static final String DATABASE_RECOMMENDATION_SCORING_NAME = "database_recommend";
    public static final String DATABASE_MISSING_MAPPING_NAME = "database_missing_mapping";
    public static final String DATABASE_LOG_MAPPING_NAME = "database_log_mapping";

    /*name of some string, convenien for change name. This can be uses:
        - as table colunm
        - in log info
    */
    public static final String SESSION_STRING = "session";
    public static final String GAMEID_STRING = "gameID";
    public static final String USERID_STRING = "userID";
    public static final String AMOUNT_STRING = "amount";
    public static final String WEEK_STRING = "week";
    public static final String LOGIN_TIMES_STRING = "login_times";
    public static final String SCORE_STRING = "score";
    public static final String DATE_STRING = "date";
}
