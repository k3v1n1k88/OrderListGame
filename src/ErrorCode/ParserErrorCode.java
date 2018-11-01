/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ErrorCode;

/**
 *
 * @author root
 */
public final class ParserErrorCode {
    
    public static final String MISSING_1 = "Some error happened with your log message. We cant find session or userID or gameID in your log.";
    public static final String MISSING_2_1 = "Some error happened with your log message. We cant find userID or amount or gameID in your log.";
    public static final String MISSING_2_2 = "Some error happened with your log message. We cant not parse your amount to number.";
    private ParserErrorCode(){}
}
