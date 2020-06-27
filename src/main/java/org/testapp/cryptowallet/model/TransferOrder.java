package org.testapp.cryptowallet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferOrder implements InOrder, OutOrder {

	private String sourceWalletId;
	
	private String sourceCurrencySymbol;
	
	private BigDecimal sourceAmount;
	
	private String targetWalletId;
	
	private String targetCurrencySymbol;
	
	private BigDecimal targetAmount;
	
	private LocalDateTime date;

	public String getSourceWalletId() {
		return sourceWalletId;
	}

	public void setSourceWalletId(String sourceWalletId) {
		this.sourceWalletId = sourceWalletId;
	}

	@Override
	public String getSourceCurrencySymbol() {
		return sourceCurrencySymbol;
	}

	@Override
	public void setSourceCurrencySymbol(String sourceCurrency) {
		this.sourceCurrencySymbol = sourceCurrency;
	}

	@Override
	public BigDecimal getSourceAmount() {
		return sourceAmount;
	}

	@Override
	public void setSourceAmount(BigDecimal sourceAmount) {
		this.sourceAmount = sourceAmount;
	}

	public String getTargetWalletId() {
		return targetWalletId;
	}

	public void setTargetWalletId(String targetWalletId) {
		this.targetWalletId = targetWalletId;
	}

	public String getTargetCurrencySymbol() {
		return targetCurrencySymbol;
	}

	public void setTargetCurrencySymbol(String targetCurrency) {
		this.targetCurrencySymbol = targetCurrency;
	}

	@JsonProperty
	public BigDecimal getTargetAmount() {
		return targetAmount;
	}
	
	@JsonIgnore
	public void setTargetAmount(BigDecimal targetAmount) {
		this.targetAmount = targetAmount;
	}

	@Override
	@JsonProperty
	public LocalDateTime getDate() {
		return date;
	}

	@JsonIgnore
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
}
