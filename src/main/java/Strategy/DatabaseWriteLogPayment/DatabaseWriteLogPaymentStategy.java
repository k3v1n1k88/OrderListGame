/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.DatabaseWriteLogPayment;

import Object.LogPayment;

/**
 *
 * @author root
 */
public interface DatabaseWriteLogPaymentStategy {
    void writeLog(LogPayment logPayment);
}
