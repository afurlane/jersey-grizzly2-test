package org.example.infrastructure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.xml.bind.annotation.XmlTransient;

public class SwaggerOASMoneyMapperProcessor implements io.swagger.v3.oas.integration.api.ObjectMapperProcessor {

    @Override
    public void processJsonObjectMapper(ObjectMapper objectMapper) {
        addMixIns( objectMapper );
    }

    private void addMixIns(ObjectMapper objectMapper) {
        objectMapper.addMixIn(MonetaryAmount.class , MixIn.class );
    }

    public abstract class MixIn {
        @JsonIgnore
        @XmlTransient
        public abstract MonetaryAmountFactory<? extends MonetaryAmount> getFactory();
    }
}
