package org.example.infrastructure.transactional;

import com.arjuna.ats.arjuna.coordinator.TransactionReaper;
import com.arjuna.ats.arjuna.recovery.RecoveryManager;
import com.arjuna.ats.jdbc.common.jdbcPropertyManager;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;

import javax.naming.Context;
import java.io.IOException;

public class LifeCycleListener implements ContainerRequestFilter, ContainerResponseFilter {

    RecoveryManager recoveryManager;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        // run the reaper here so it is in the server's classloader context
        // if we started it lazily on first tx it would run with context of the webapp using the tx.
        // run the reaper here so it is in the server's classloader context
        // if we started it lazily on first tx it would run with context of the webapp using the tx.
        TransactionReaper.instantiate();

        // recovery needs the correct JNDI settings.
        // XADataSourceWrapper sets these too as a precaution, but we may run first.
        jdbcPropertyManager.getJDBCEnvironmentBean().getJndiProperties().put("Context.INITIAL_CONTEXT_FACTORY", System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        jdbcPropertyManager.getJDBCEnvironmentBean().getJndiProperties().put("Context.URL_PKG_PREFIXES", System.getProperty(Context.URL_PKG_PREFIXES));

        // a 'start' occurs after the Resources in GlobalNamingResources are instantiated,
        // so we can safely start the recovery Thread here.
        recoveryManager = RecoveryManager.manager();
        recoveryManager.startRecoveryManagerThread();
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        // End transaction
        recoveryManager.terminate();
    }
}
