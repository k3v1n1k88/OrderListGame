/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

/**
 *
 * @author root
 */
public interface DatabaseConnection {
    
    void createConnection() throws Exception;
    
    void close()throws Exception;
    
    boolean ping();
}
