package ru.eamosov.n26.jetty;

import org.eclipse.jetty.server.Server;
import ru.eamosov.n26.impl.StatsServiceImpl;
import ru.eamosov.n26.rest.StatisticsController;
import ru.eamosov.n26.rest.TransactionController;

/**
 * JettyServer
 */
public class JettyServer {

    private final Server jetty;

    public JettyServer(int port) {
        final StatsServiceImpl statsService = new StatsServiceImpl();

        final JettyRestHandler jettyRestHandler = new JettyRestHandler();

        jettyRestHandler.registerController(new TransactionController(statsService));
        jettyRestHandler.registerController(new StatisticsController(statsService));

        jetty = new Server(port);
        jetty.setHandler(jettyRestHandler);
    }

    public void start() throws Exception {
        jetty.start();
    }

    public void stop() throws Exception {
        jetty.stop();
    }
}
