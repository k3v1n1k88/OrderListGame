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
public final class DBInfo {
    
    public static final int port = 7777;
    
    public static final String userName = "root";
    public static final String password = "nhakhoahoc";
    public static final String databaseName = "GameListOrder";
    public static final String hostName = "localhost";
    
    public static final String topScoreGameTableString = "TopScoreGame";
    public static final String pointOfGameTableString = "PointOfGame";
    public static final String logMissingTableString = "MissMappingLog";
    public static final String gameIDString = "GameID";
    public static final String pointString = "Point";
    public static final String sessionString = "Session";
    public static final String userIDString = "UserID";
    public static final String amountString = "Amount";
    public static final String weekString = "Week";
    public static final String loginTimesString = "LoginTimes";
    
    
    public static final String timeString = "Time";
    
    public static final String orderListGame = "oderlist";
    public static final String recommentListGame = "recommentList";
    public static final int numsRecommentGame = 5;
    public static final String databaseMissMappingName = "MissMapping";
    public static final String databaseMappingName = "DatabaseMapping";
    public static final String databaseRecommentName = "DatabaseRecomment";
    private DBInfo(){
    }
}
