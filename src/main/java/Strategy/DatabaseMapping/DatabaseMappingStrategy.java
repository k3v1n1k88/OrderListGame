/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.DatabaseMapping;

import Log.LogPayment;

/**
 *
 * @author root
 */
public interface DatabaseMappingStrategy {
    void writeMappedInfo(String session, LogPayment logPayment) throws Exception;
}
