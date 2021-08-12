package org.example.infrastructure.mapper;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;

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
