/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connection;

import exception.PoolException;
import java.util.Set;

/**
 *
 * @author root
 * @param <T>: database connection
 */
public interface DatabaseConnectionPool<T extends DatabaseConnection> {
    
    public T borrowObjectFromPool() throws PoolException;
    
    public void returnObjectToPool(T obj) throws PoolException;
    
    public void invalidateObjectOfPool(T obj) throws PoolException;
    
    public void closePool() throws PoolException;
    
    public void clearPool() throws PoolException;
    
    public boolean isClosedPool();
 
}
