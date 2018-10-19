/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import Strategy.DatabasePerform.DatabasePerformMySQL;
import Strategy.DatabasePerform.DatabasePerformStrategy;
import java.io.IOException;

/**
 *
 * @author root
 */
public class RequestOrderList {
    private String session;
    private DatabasePerformStrategy databasePerformStrategy;
    
    public RequestOrderList(String session){
        
    }
    
    public RequestOrderList(String session, DatabasePerformStrategy databasePerformStrategy){
        this.session = session;
        this.databasePerformStrategy = databasePerformStrategy;
    }
    
    public String getOrderListGame() throws Exception{
        return databasePerformStrategy.getList(session);
    }
}
