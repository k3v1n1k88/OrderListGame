/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy.database.mapping.missing;

import object.log.LogPayment;

/**
 *
 * @author root
 */
public interface DatabaseMappingMissingStrategy {
    void writeLogToDatabase(LogPayment logPayment) throws Exception;
}
