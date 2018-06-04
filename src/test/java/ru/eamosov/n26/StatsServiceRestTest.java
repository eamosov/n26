package ru.eamosov.n26;

import org.junit.After;
import org.junit.Before;
import ru.eamosov.n26.api.StatsService;
import ru.eamosov.n26.impl.StatsServiceRest;
import ru.eamosov.n26.jetty.JettyServer;

/**
 * Test {@link StatsService} as REST service
 */
public class StatsServiceRestTest extends StatsServiceTest {

    static int port = 8080;

    private JettyServer jettyServer;
    private StatsService statsService = new StatsServiceRest("localhost", port);

    public StatsServiceRestTest() {
    }

    protected StatsService service() {
        return statsService;
    }

    @Before
    public void startRestService() throws Exception {
        jettyServer = new JettyServer(port);
        jettyServer.start();
    }

    @After
    public void stopRestService() throws Exception {
        jettyServer.stop();
    }
}
