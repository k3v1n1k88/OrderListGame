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
 */
public interface DatabaseConnectionPool {
    public Object borrowObject() throws Exception;
    public void returnObject(Object obj);
    public void invalidateObject(Object obj) throws Exception;
    public void close();
    public void clear();
    public boolean isClosed();
    public Set<Object> listAllObject();
}
