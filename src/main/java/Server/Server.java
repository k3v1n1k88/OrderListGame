/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import thrift.OrderListGame;

/**
 *
 * @author root
 */
public class Server {
    
    private final int port;
    private final String host;
    private final int minWorker;
    private final int maxWorker;
    private final int requestTimeout;
    
    private final OrderListGame.Iface handler;
    
    TServer server;
    private TThreadPoolServer.Args agrs;
    
    private Server(int port,OrderListGame.Iface handler){
        this("localhost",port,20,100,20,handler);
    }
    private Server(String host, int port ,int minWorker, int maxWorker,int requestTimeout, OrderListGame.Iface handler){
        this.host = host;
        this.port = port;
        this.minWorker = minWorker;
        this.maxWorker = maxWorker;
        this.requestTimeout = requestTimeout;
        this.handler = handler;
    }
    public void start() throws TTransportException{
        OrderListGame.Processor processor = new OrderListGame.Processor(handler);
        InetSocketAddress binArr = new InetSocketAddress(this.host,this.port);
        TServerTransport serverTransport = new TServerSocket(this.port);
        this.agrs = new TThreadPoolServer.Args(serverTransport)
                                        .processor(processor)
                                        .maxWorkerThreads(this.maxWorker)
                                        .minWorkerThreads(this.minWorker)
                                        .protocolFactory(new TBinaryProtocol.Factory())
                                        .transportFactory(new TFramedTransport.Factory())
                                        .requestTimeout(requestTimeout);
        this.server = new TThreadPoolServer(this.agrs);
        this.server.serve();
    }
    public void stop(){
        this.server.stop();
    }
    public static void main(String[] args) throws TTransportException {
        Server server = new Server(9090, new OrderListGameImpl());
        System.out.println("Server starting...");
        server.start();
    }
}
