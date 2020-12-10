package org.example;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.infrastructure.HttpSessionFactory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import java.net.URI;
import java.util.logging.Level;

public class CdiBaseTest extends JerseyTest {

    protected Logger log = LogManager.getLogger();

    SeContainerInitializer containerInit = SeContainerInitializer.newInstance();
    SeContainer container = containerInit.initialize();

    @Override
    public void setUp() throws Exception {
        containerInit = SeContainerInitializer.newInstance();
        container = containerInit.initialize();
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected URI getBaseUri() {
        return UriBuilder.fromUri(super.getBaseUri()).build();
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    @Override
    protected DeploymentContext configureDeployment() {
        ResourceConfig config = new ResourceConfig().forApplicationClass(MyApplication.class)
                .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.FINEST.getName())
                .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, Level.FINEST.getName());
        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(HttpSessionFactory.class).to(HttpSession.class);
            }
        });
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return ServletDeploymentContext.forServlet(
                new ServletContainer(config)).build();
    }
}
