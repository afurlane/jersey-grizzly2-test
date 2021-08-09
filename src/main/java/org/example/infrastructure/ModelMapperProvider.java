package org.example.infrastructure;

import org.example.infrastructure.jpa.MonetaryAmountToEntityConverter;
import org.example.infrastructure.jpa.MonetaryEntityToAmountConverter;
import org.modelmapper.ModelMapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class ModelMapperProvider implements ContextResolver<ModelMapper> {

    private final ModelMapper modelMapper;

    public ModelMapperProvider() {
        modelMapper = new ModelMapper();
        modelMapper.addConverter(new MonetaryEntityToAmountConverter());
        modelMapper.addConverter(new MonetaryAmountToEntityConverter());
    }

    @Override
    public ModelMapper getContext(Class<?> type) {
        return modelMapper;
    }
}
