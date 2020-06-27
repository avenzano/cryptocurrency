package org.testapp.cryptowallet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface InOrder {

	String getTargetCurrencySymbol();

	void setTargetCurrencySymbol(String targetCurrency);

	BigDecimal getTargetAmount();

	void setTargetAmount(BigDecimal targetAmount);

	LocalDateTime getDate();

	void setDate(LocalDateTime date);

}