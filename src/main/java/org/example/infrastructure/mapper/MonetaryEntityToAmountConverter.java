package org.example.infrastructure.mapper;

import org.example.entities.MonetaryEntity;
import org.javamoney.moneta.Money;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import javax.money.MonetaryAmount;

public class MonetaryEntityToAmountConverter implements Converter<MonetaryEntity, MonetaryAmount> {

    @Override
    public MonetaryAmount convert(MappingContext<MonetaryEntity, MonetaryAmount> mappingContext) {
        return Money.of(mappingContext.getSource().getAmount(), mappingContext.getSource().getCurrency());
    }
}
