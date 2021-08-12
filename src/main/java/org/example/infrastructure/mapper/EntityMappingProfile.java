package org.example.infrastructure.mapper;

import jakarta.inject.Singleton;
import org.example.entities.ExampleDetailEntity;
import org.example.entities.ExampleEntity;
import org.example.controllers.webapi.models.ExampleDetailModel;
import org.example.controllers.webapi.models.ExampleModel;
import org.modelmapper.ModelMapper;

@Singleton
public class EntityMappingProfile implements MappingProfile {

    private final MonetaryAmountToEntityConverter monetaryAmountToEntityConverter = new MonetaryAmountToEntityConverter();
    private final MonetaryEntityToAmountConverter monetaryEntityToAmountConverter = new MonetaryEntityToAmountConverter();
    @Override
    public ModelMapper configure(ModelMapper modelMapper) {
        modelMapper.addConverter(monetaryAmountToEntityConverter);
        modelMapper.addConverter(monetaryEntityToAmountConverter);
        modelMapper.createTypeMap(ExampleEntity.class, ExampleModel.class)
                .addMappings(mapper -> {
            mapper.map(src -> src.getExampleDetailEntity(), ExampleModel::setExampleDetailModels);
            mapper.map(src -> src.getName(), ExampleModel::setName);
            mapper.map(src -> src.getAmount(), ExampleModel::setAmount);
            mapper.map(src -> src.getId(), ExampleModel::setId);
        });
        modelMapper.createTypeMap(ExampleDetailEntity.class, ExampleDetailModel.class).addMappings(mapper -> {
            mapper.map(src -> src.getId(), ExampleDetailModel::setId);
            mapper.map(src -> src.getName(), ExampleDetailModel::setName);
        });

        return modelMapper;
    }
}
