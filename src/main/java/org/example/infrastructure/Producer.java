package org.example.infrastructure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entities.ExampleDetailEntity;
import org.example.entities.ExampleEntity;
import org.example.models.ExampleDetailModel;
import org.example.models.ExampleModel;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Singleton
public class Producer {

    // Interesting
    // https://stackoverflow.com/questions/21781026/how-to-send-java-util-logging-to-log4j2

    private ModelMapper modelMapper;

    @Produces
    public Logger getLogger(InjectionPoint p)
    {
        return LogManager.getLogger(p.getClass().getCanonicalName());
    }

    @Produces
    public EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("example-unit");
    }

    @Produces
    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    @PostConstruct
    public void InitDefaults() {
        modelMapper = new ModelMapper();
        modelMapper.createTypeMap(ExampleEntity.class, ExampleModel.class).addMappings(mapper -> {
            mapper.map(src -> src.getExampleDetailEntity(), ExampleModel::setExampleDetailModels);
            mapper.map(src -> src.getName(), ExampleModel::setName);
            mapper.map(src -> src.getId(), ExampleModel::setId);
        });
        modelMapper.createTypeMap(ExampleDetailEntity.class, ExampleDetailModel.class).addMappings(mapper -> {
           mapper.map(src -> src.getId(), ExampleDetailModel::setId);
           mapper.map(src -> src.getName(), ExampleDetailModel::setName);
        });
    }
}
