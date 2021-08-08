package org.example.infrastructure;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.jackson.AbstractModelConverter;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.Iterator;

public class SwaggerMonetaryAmountConverter extends AbstractModelConverter {
    public SwaggerMonetaryAmountConverter(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (type.isSchemaProperty()) {
            JavaType _type = Json.mapper().constructType(type.getType());
            if (_type != null) {
                Class<?> cls = _type.getRawClass();
                if (MonetaryAmount.class.isAssignableFrom(cls)) {
                    return new Schema<MonetaryWrapper>();
                }
            }
        }
        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        } else {
            return null;
        }
    }

    private class MonetaryWrapper {
        private BigDecimal amount;
        private String currency;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}