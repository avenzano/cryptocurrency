package org.testapp.cryptowallet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

/**
 * A {@link User}'s deposit from a bank account
 * (or maybe anoter source of money?)
 * 
 * This entity represents an already completed deposit.
 * The amount described by te deposit is avilable to
 * be exchanged.
 * 
 * @author avenzano
 */
@ApiModel(value="Deposit in a User's account")
public class Deposit {

	@ApiModelProperty(required = true, value = "The Id of a transaction made to the company")
	private String transactionId;
	
	@ApiModelProperty(required = true, value = "The transfer's currency")
	private SimpleCurrency currency;
	
	@ApiModelProperty(required = true, value = "The actual amount transferred")
	private BigDecimal amount;
	
	@ApiModelProperty(accessMode = AccessMode.READ_ONLY, value = "The moment of this deposit's consolidation")
	private LocalDateTime consolidationDate;
	
	@ApiModelProperty(accessMode = AccessMode.READ_ONLY, value = "The user that approved the deposit")
	private String responsible;

	public Deposit() {		
	}
	
	public Deposit(String transactionId, SimpleCurrency currency, BigDecimal amount) {
		super();
		this.transactionId = transactionId;
		this.currency = currency;
		this.amount = amount;
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public SimpleCurrency getCurrency() {
		return currency;
	}

	public void setCurrency(SimpleCurrency currency) {
		this.currency = currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getConsolidationDate() {
		return consolidationDate;
	}

	public void setConsolidationDate(LocalDateTime moment) {
		this.consolidationDate = moment;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	
}
