package org.testapp.cryptowallet.cryptocompare.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(UpperCamelCaseStrategy.class)
public class CoinData {

	private String id;
	
	private String coinName;
	
	private String fullName;
	
	private String symbol;
	
	private String algorithm;
	
	private boolean isTrading;
	
	private BigDecimal totalCoinsMined;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public boolean isTrading() {
		return isTrading;
	}

	public void setTrading(boolean isTrading) {
		this.isTrading = isTrading;
	}

	public BigDecimal getTotalCoinsMined() {
		return totalCoinsMined;
	}

	public void setTotalCoinsMined(BigDecimal totalCoinsMined) {
		this.totalCoinsMined = totalCoinsMined;
	}
	
}
