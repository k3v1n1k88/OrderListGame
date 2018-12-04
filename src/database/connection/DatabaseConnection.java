/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connection;

import exception.DatabaseException;

/**
 *
 * @author root
 */
public interface DatabaseConnection {
    
    void createConnection() throws DatabaseException;
    
    void close()throws DatabaseException;
    
    boolean ping();
}
