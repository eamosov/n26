package ru.eamosov.n26;

import ru.eamosov.n26.jetty.JettyServer;

/**
 * Main
 */
public class Main {

    public static void main(String argv[]) throws Exception {


        final JettyServer jettyServer = new JettyServer(8080);
        jettyServer.start();

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }

        jettyServer.stop();
    }
}
