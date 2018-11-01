/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import VNG.generate.GenerateRequestFromPayment;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import thrift.OrderListGame;

/**
 *
 * @author root
 */
public class Client {

    public static void main(String[] args) throws TTransportException, TException {
        TSocket socket = new TSocket("localhost", 9090);
        TTransport transport = new TFramedTransport(socket);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        OrderListGame.Client client = new OrderListGame.Client(protocol);
        //System.out.println(client.getList("72kj8aj19a"));
        //client.pushLogLogin(GenerateRequestFromLogin.get());
        client.pushLogPayment(GenerateRequestFromPayment.get());
        transport.close();
    }
}
