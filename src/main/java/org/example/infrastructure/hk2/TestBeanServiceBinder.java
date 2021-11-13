package org.example.infrastructure.hk2;

import org.example.services.TestBeanServiceImplementation;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class TestBeanServiceBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(TestBeanServiceBinder.class).to(TestBeanServiceImplementation.class);
    }
}
