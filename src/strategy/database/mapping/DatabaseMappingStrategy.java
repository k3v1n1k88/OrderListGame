/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy.database.mapping;

import object.log.LogPayment;

/**
 *
 * @author root
 */
public interface DatabaseMappingStrategy {
    void writeMappedInfo(String session, LogPayment logPayment) throws Exception;
}
