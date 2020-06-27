package org.testapp.cryptowallet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BuyOrder implements InOrder {

	private String walletId;
	
	private SimpleCurrency sourceCurrency;
	
	private BigDecimal sourceAmount;
	
	private String targetCurrencySymbol;
	
	private BigDecimal targetAmount;
	
	private LocalDateTime date;

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public SimpleCurrency getSourceCurrency() {
		return sourceCurrency;
	}

	public void setSourceCurrency(SimpleCurrency sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}

	public BigDecimal getSourceAmount() {
		return sourceAmount;
	}

	public void setSourceAmount(BigDecimal sourceAmount) {
		this.sourceAmount = sourceAmount;
	}

	@Override
	public String getTargetCurrencySymbol() {
		return targetCurrencySymbol;
	}

	@Override
	public void setTargetCurrencySymbol(String currencySymbol) {
		this.targetCurrencySymbol = currencySymbol;
	}

	@Override
	@JsonProperty
	public BigDecimal getTargetAmount() {
		return targetAmount;
	}

	@Override
	@JsonIgnore
	public void setTargetAmount(BigDecimal targetAmount) {
		this.targetAmount = targetAmount;
	}

	@Override
	@JsonProperty
	public LocalDateTime getDate() {
		return date;
	}

	@Override
	@JsonIgnore
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
}
