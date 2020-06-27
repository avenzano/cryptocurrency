package org.testapp.cryptowallet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OutOrder {

	String getSourceCurrencySymbol();

	void setSourceCurrencySymbol(String sourceCurrency);

	BigDecimal getSourceAmount();

	void setSourceAmount(BigDecimal sourceAmount);

	LocalDateTime getDate();

}