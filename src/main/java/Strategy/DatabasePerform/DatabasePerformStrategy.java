/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.DatabasePerform;

import Object.LogLogin;
import Object.LogPayment;

/**
 *
 * @author root
 */
public interface DatabasePerformStrategy {
    void accessToDatabase(LogLogin logLogin) throws Exception;
    void mappingToDatabase(LogPayment logPayment) throws Exception;
    String getList(String sesion) throws Exception;
}
