package org.example.entities;

import org.hibernate.validator.constraints.Length;

import javax.money.MonetaryAmount;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class MonetaryEntity {
    private BigDecimal Amount;
    @Column(length=4)
    private String Currency;

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public void setAmount(MonetaryAmount amount) {
        Amount = amount.getNumber().numberValue(BigDecimal.class);
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public void setCurrency(MonetaryAmount amount) {
        Currency = amount.getCurrency().getCurrencyCode();
    }

    public void setFromMonetaryAmount(MonetaryAmount amount) {
        Currency = amount.getCurrency().getCurrencyCode();
        Amount = amount.getNumber().numberValue(BigDecimal.class);
    }
}
