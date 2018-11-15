/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bussiness;

import configuration.ConfigServer;
import handler.LandingPageController;
import handler.LogLoginController;
import handler.LogPaymentController;
import handler.RecommendationController;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 *
 * @author root
 */
public class ListOrderGameServer implements Runnable{
    
    private static final Logger logger = Logger.getLogger(ListOrderGameServer.class);
    
    private final Server server = new Server();
    
    private static ListOrderGameServer _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    public static ServerConnector connector;

    public static ListOrderGameServer getInstance() {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new ListOrderGameServer();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    @Override
    public void run() {
        try {
            this.connector = new ServerConnector(server);
            ConfigServer configServer = new ConfigServer(constant.PathConstant.PATH_TO_SERVER_CONFIG_FILE);
            connector.setPort(configServer.getPort());
            connector.setHost(configServer.getHost());
            connector.setIdleTimeout(30000);
            server.setConnectors(new Connector[]{connector});

            ServletHandler servletHandler = new ServletHandler();
            servletHandler.addServletWithMapping(LogLoginController.class, "/log/login");
            servletHandler.addServletWithMapping(LogPaymentController.class, "/log/payment");
            servletHandler.addServletWithMapping(LandingPageController.class, "/get");
            servletHandler.addServletWithMapping(RecommendationController.class, "/getList");

            ResourceHandler resource_handler = new ResourceHandler();
            resource_handler.setResourceBase("./static/");

            ContextHandler resourceContext = new ContextHandler();
            resourceContext.setContextPath("/web/static");
            resourceContext.setHandler(resource_handler);

            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{resourceContext, servletHandler, new DefaultHandler()});

            server.setHandler(handlers);
            server.start();
            server.join();
        } catch (Exception ex) {
            System.out.println(ex);
            logger.error(ex.getMessage(), ex);
        }
    }

    public void stop() throws Exception {
        server.stop();
    }
}
