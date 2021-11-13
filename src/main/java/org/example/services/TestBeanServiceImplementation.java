package org.example.services;

import jakarta.annotation.ManagedBean;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.logging.log4j.Logger;

@ManagedBean
public class TestBeanServiceImplementation implements TestBeanService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Logger logger;

    public void testMethod() {
        if (entityManager != null)
            logger.info("The persistenceContext is set");
    }
}
