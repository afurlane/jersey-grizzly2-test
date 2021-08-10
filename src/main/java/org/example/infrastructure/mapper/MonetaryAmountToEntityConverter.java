package org.example.infrastructure.mapper;

import org.example.entities.MonetaryEntity;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

public class MonetaryAmountToEntityConverter implements Converter<MonetaryAmount, MonetaryEntity> {

    @Override
    public MonetaryEntity convert(MappingContext<MonetaryAmount, MonetaryEntity> mappingContext) {
        MonetaryEntity monetaryEntity = new MonetaryEntity();
        monetaryEntity.setCurrency(mappingContext.getSource().getCurrency().getCurrencyCode());
        monetaryEntity.setAmount((BigDecimal)mappingContext.getSource().getNumber().numberValue(BigDecimal.class));
        return monetaryEntity;
    }
}
