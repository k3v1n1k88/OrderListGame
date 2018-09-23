/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Exception.ParseLogException;
import Object.LogLogin;
import Object.LogPayment;
import Object.RequestOrderList;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.json.simple.parser.ParseException;
import thrift.OrderListGame;

/**
 *
 * @author root
 */
public class OrderListGameImpl implements OrderListGame.Iface{
    private static final Logger LOGGER = Logger.getLogger(OrderListGameImpl.class);
    
    @Override
    public String getList(String session) throws TException {
       RequestOrderList requestOrderList = new RequestOrderList(session);
        try {
            LOGGER.info("Get request get list with season: "+session);
            return requestOrderList.getOrderListGame();
        } catch (Exception ex) {
            throw new TException(ex);
        }
    }

    @Override
    public boolean pushLogPayment(String log) throws TException {
        boolean res = true;
        try {
            LogPayment logPayment = LogPayment.logToObj(log);
            logPayment.mappingDatabase();
            LOGGER.info("Get push request with log:" +log);
            return res;
        } catch (ParseException | ParseLogException | SQLException ex) {       
            throw new TException(ex);
        } catch (Exception ex) {
            throw new TException(ex);
        }
    }

    @Override
    public boolean pushLogLogin(String log) throws TException {
        boolean res = true;
        try {
            LogLogin logLogin = LogLogin.logToObj(log);
            logLogin.accessToDatabase();
            LOGGER.info("Get push request with log:" +log);
            return res;
        } catch (ParseException | ParseLogException | SQLException ex) {       
            throw new TException(ex);
        } catch (Exception ex) {
            throw new TException(ex);
        }
    }
    
}
