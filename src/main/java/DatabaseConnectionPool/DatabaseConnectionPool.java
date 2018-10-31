/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseConnectionPool;

import java.util.Set;

/**
 *
 * @author root
 * @param <T>: database connection
 */
public interface DatabaseConnectionPool<T extends DatabaseConnection> {
    
    public T borrowObjectFromPool() throws Exception;
    
    public void returnObjectToPool(T obj) throws Exception;
    
    public void invalidateObjectOfPool(T obj) throws Exception;
    
    public void closePool() throws Exception;
    
    public void clearPool() throws Exception;
    
    public boolean isClosedPool();
 
}
