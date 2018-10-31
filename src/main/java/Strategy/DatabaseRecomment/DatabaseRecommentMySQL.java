///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package Strategy.DatabaseRecomment;
//
//
//import DatabaseConnectionPool.DatabaseConnectionPoolMySQL;
//import DatabaseConnectionPool.DatabaseInfo;
//import DatabaseConnectionPool.DatabaseMySQLConnection;
//import DatabaseConnectionPool.DatabaseMySQLConnectionFactory;
//import com.google.gson.JsonArray;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.List;
//import org.apache.commons.pool2.impl.GenericObjectPool;
//import org.apache.log4j.Logger;
//
///**
// *
// * @author root
// */
//public class DatabaseRecommentMySQL implements DatabaseRecommentStrategy{
//
//    private static final long serialVersionUID = 384578475845L;
//    private static final Logger LOGGER = Logger.getLogger(DatabaseRecommentMySQL.class);
//    private static GenericObjectPool pool = null; 
//    
//    private DatabaseInfo databaseInfo;
//    
//    public DatabaseRecommentMySQL(String hostAddress,int port,String databaseName,String userName,String password){
//        this(new DatabaseInfo(hostAddress,port,databaseName,userName,password));
//        
//    }
//    
//    public DatabaseRecommentMySQL(DatabaseInfo databaseInfo){
//        this.databaseInfo = databaseInfo;
//        if(DatabaseRecommentMySQL.pool == null){
//            DatabaseMySQLConnectionFactory factory = new DatabaseMySQLConnectionFactory(databaseInfo.getHostAddress(),
//                                                                                        databaseInfo.getPort(),
//                                                                                        databaseInfo.getDatabaseName(),
//                                                                                        databaseInfo.getUserName(),
//                                                                                        databaseInfo.getPassword());
//            DatabaseRecommentMySQL.pool = new DatabaseConnectionPoolMySQL(factory);
//        }
//    }
//    
//    
//    @Override
//    public int addGameIDEvenExist(String gameID) throws Exception {
//        Connection cnn;
//        DatabaseMySQLConnection databaseMySQLConnection;
//        PreparedStatement preparedStatement = null;
//        try {
//            databaseMySQLConnection = (DatabaseMySQLConnection) DatabaseRecommentMySQL.pool.borrowObject();
//            cnn = databaseMySQLConnection.getConnection();
//        } catch (Exception ex) {
//            LOGGER.error("Error happen when borrow object from pool");
//            throw ex;
//        }
//        try {
//            String format = "INSERT IGNORE INTO ?(?)"+
//                            " VALUES(\'?\')";
//            
//            preparedStatement = cnn.prepareStatement(format);
//            preparedStatement.setString(1,DBInfo.pointOfGameTableString);
//            preparedStatement.setString(2,DBInfo.gameIDString);
//            preparedStatement.setString(3,gameID);
//            
//            int affectRows = preparedStatement.executeUpdate();
//            return affectRows;
//        } catch (SQLException ex) {
//            LOGGER.error("Error with SQL statement: "+preparedStatement+".\nDetail:"+ex);
//            if(cnn.isClosed()){
//                DatabaseRecommentMySQL.pool.invalidateObject(databaseMySQLConnection);
//                databaseMySQLConnection = null;
//            }
//            throw ex;
//        }finally{
//            if(databaseMySQLConnection != null){
//                DatabaseRecommentMySQL.pool.returnObject(databaseMySQLConnection);
//            }
//        }
//    }
//
//    @Override
//    public int update(List<String> listGame) throws Exception, SQLException {
//        Connection cnn;
//        DatabaseMySQLConnection databaseMySQLConnection;
//        PreparedStatement preparedStatment = null;
//        try {
//            databaseMySQLConnection = (DatabaseMySQLConnection) DatabaseRecommentMySQL.pool.borrowObject();
//            cnn = databaseMySQLConnection.getConnection();
//        } catch (Exception ex) {
//            LOGGER.error("Error happen when borrow object from pool");
//            throw ex;
//        }
//        try {
//            int affectRows = 0;
//            String format = "UPDATE ?"+
//                            " SET ? = ? + ?"+
//                            " WHERE ? = ?";
//            for(int i=0;i<listGame.size();i++){
//                
//                long point = 0;
//                
//                preparedStatment = cnn.prepareStatement(format);
//                preparedStatment.setString(1, DBInfo.pointOfGameTableString);
//                preparedStatment.setString(2, DBInfo.pointString);
//                preparedStatment.setString(3, DBInfo.pointString);
//                preparedStatment.setLong(4, point);
//                preparedStatment.setString(5, DBInfo.gameIDString);
//                preparedStatment.setString(6, listGame.get(i));
//                
//                affectRows += preparedStatment.executeUpdate();
//            }
//            
//            return affectRows;
//        } catch (SQLException ex) {
//            LOGGER.error("Error with SQL statement: "+preparedStatment+".\nDetail:"+ex);
//            if(cnn.isClosed()){
//                DatabaseRecommentMySQL.pool.invalidateObject(databaseMySQLConnection);
//                databaseMySQLConnection = null;
//            }
//            throw ex;
//        }finally{
//            if(databaseMySQLConnection != null){
//                DatabaseRecommentMySQL.pool.returnObject(databaseMySQLConnection);
//            }
//        }
//    }
//
//    @Override
//    public JsonArray getTop(int number) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}
