package org.example.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
@Transactional
public class TestBeanServiceImplementation implements TestBeanService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Logger logger;

    @Override
    public void testMethod() {
        if (entityManager != null)
            logger.info("The persistenceContext is set");
    }
}
