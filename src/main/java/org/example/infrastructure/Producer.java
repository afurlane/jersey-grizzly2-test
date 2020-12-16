package org.example.infrastructure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManagerFactory;

public class Producer {
    // Interesting
    // https://stackoverflow.com/questions/21781026/how-to-send-java-util-logging-to-log4j2
    @Produces
    public Logger getLogger(InjectionPoint p)
    {
        return LogManager.getLogger(p.getClass().getCanonicalName());
    }

    @Produces
    public EntityManagerFactory getEntityManagerFactory() {
        return javax.persistence.Persistence.createEntityManagerFactory("example-unit");
    }
}
