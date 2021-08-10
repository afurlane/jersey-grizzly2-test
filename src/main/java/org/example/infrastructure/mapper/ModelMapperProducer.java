package org.example.infrastructure.mapper;

import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

// Just for show how to provide a mapper per context!
@Singleton
public class ModelMapperProducer {

    private final ModelMapper modelMapper;

    @Inject
    Instance<MappingProfile> mappingProfileInstance;

    public ModelMapperProducer() {
        modelMapper = new ModelMapper();
    }

    @Produces
    public ModelMapper getModelMapper (InjectionPoint p) {
        return modelMapper;
    }

    @PostConstruct
    public void InitDefaults() {
        mappingProfileInstance.forEach(mappingProfile -> mappingProfile.configure(modelMapper));
    }

}
