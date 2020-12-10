package org.example;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.jboss.weld.environment.se.Weld;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8091/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static Server startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in org.example package
        final ResourceConfig rc = new ResourceConfig().forApplicationClass(MyApplication.class);
        // .packages("io.swagger.jaxrs.listing", "org.example");
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        // return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        return JettyHttpContainerFactory.createServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // SeContainerInitializer containerInit = SeContainerInitializer.newInstance();
        // SeContainer container = containerInit.initialize();

        Weld weld = new Weld();
        weld.initialize();
        final Server server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        weld.shutdown();
    }
}

