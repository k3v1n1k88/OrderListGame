/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Exception.DatabaseException;
import Object.LogPayment;
import java.io.IOException;

/**
 *
 * @author root
 */
public interface DatabaseMappingMissingStrategy {
    void writeLogToDatabase(LogPayment logPayment) throws DatabaseException,IOException;
}
