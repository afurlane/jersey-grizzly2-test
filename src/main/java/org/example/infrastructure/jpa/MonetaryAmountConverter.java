package org.example.infrastructure.jpa;

import org.example.entities.MonetaryEntity;
import org.javamoney.moneta.Money;

import java.math.BigDecimal;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class MonetaryAmountConverter implements AttributeConverter<MonetaryAmount, MonetaryEntity> {

    @Override
    public MonetaryEntity convertToDatabaseColumn(MonetaryAmount monetaryAmount) {
        MonetaryEntity monetaryEntity = new MonetaryEntity();
        monetaryEntity.setAmount(monetaryAmount.getNumber().numberValue(BigDecimal.class));
        monetaryEntity.setCurrency(monetaryAmount.getCurrency().getCurrencyCode());
        return monetaryEntity;
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(MonetaryEntity monetaryEntity) {
        return Money.of(monetaryEntity.getAmount(), monetaryEntity.getCurrency());
    }
}