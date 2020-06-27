package org.testapp.cryptowallet.model;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="The User's account")
public class Account {

	@ApiModelProperty(value="The Account's currency")
	private SimpleCurrency currency;
	
	@ApiModelProperty(value="The Account's current balance")
	private BigDecimal balance;

	public Account() {}
	
	public Account(SimpleCurrency curr, BigDecimal initialBalance) {
		this.currency = curr ;
		this.balance = initialBalance;
	}
	
	public SimpleCurrency getCurrency() {
		return currency;
	}

	public void setCurrency(SimpleCurrency currency) {
		this.currency = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void add(BigDecimal amount) {
		balance = balance.add(amount);
	}
	
	public void subtract(BigDecimal amount) {
		balance = balance.subtract(amount);
	}
	
}
