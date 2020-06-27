package org.testapp.cryptowallet.model;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testapp.cryptowallet.exception.InsufficientFundsException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

@ApiModel(value = "CryptoWallet's information and currencies")
public class CryptoWallet {

	@ApiModelProperty(accessMode = AccessMode.READ_ONLY, value = "The wallet's indentifier value")
	private String id;
	
	@ApiModelProperty(accessMode = AccessMode.READ_ONLY, value = "Wallet's owner username")
	private String username;
	
	@ApiModelProperty(value = "Wallet's fiendly name (can be changed)")
	private String friendlyName;
	
	@ApiModelProperty(value = "Wallet's creation date")
	private LocalDateTime created;
	
	@ApiModelProperty(value = "The wallet's stored values")
	private Map<String, BigDecimal> currencies = new HashMap<>();
	
	@JsonIgnore
	private List<InOrder> inOrders = new ArrayList<>();
	
	@JsonIgnore
	private List<OutOrder> outOrders = new ArrayList<>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String privateKey) {
		this.username = privateKey;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String publicKey) {
		this.friendlyName = publicKey;
	}

	@JsonProperty
	public LocalDateTime getCreated() {
		return created;
	}

	@JsonIgnore
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public Map<String, BigDecimal> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Map<String, BigDecimal> currencies) {
		this.currencies = currencies;
	}
	
	public boolean hasCurrency(String currencySymbol) {
		return this.currencies.containsKey(currencySymbol);
	}

	public List<InOrder> getInOrders() {
		return inOrders;
	}

	public void setInOrders(List<InOrder> buyOrders) {
		this.inOrders = buyOrders;
	}
	
	public List<OutOrder> getOutOrders() {
		return outOrders;
	}

	public void setOutOrders(List<OutOrder> orders) {
		this.outOrders = orders;
	}

	public <T extends InOrder> T execute(T order) {
		add(order.getTargetCurrencySymbol(), order.getTargetAmount());
		order.setDate(LocalDateTime.now());
		inOrders.add(order);
		// on a real app we should return a copy
		return order;
	}
	
	public BigDecimal add(String currencySymbol, BigDecimal amount) {
		BigDecimal balance = currencies.get(currencySymbol);
		if ( balance == null) {
			balance = BigDecimal.ZERO;
		}
		balance = balance.add(amount);
		currencies.put(currencySymbol, balance);
		return balance;
	}
	
	public BigDecimal subtract(String currencySymbol, BigDecimal amount) {
		return add(currencySymbol, amount.negate());
	}

	public boolean hasCurrency(String currencySymbol, BigDecimal amount) {
		if ( !hasCurrency(currencySymbol)) {
			return false;
		}
		return currencies.get(currencySymbol).compareTo(amount) >= 0;
	}

	public <T extends InOrder&OutOrder> T execute(T order, CryptoWallet tgtWallet) {
		if ( ! hasCurrency(order.getSourceCurrencySymbol(), order.getSourceAmount()) ) {
			throw new InsufficientFundsException("There are not enough funds on currency "+order.getSourceCurrencySymbol());
		}
		subtract(order.getSourceCurrencySymbol(), order.getSourceAmount());
		outOrders.add(order);
		T processed = tgtWallet.execute(order);
		
		return processed;
	}

	public boolean hasValues() {
		// note that empty currencies return false
		return currencies.values().stream().anyMatch( val -> val != null && val.compareTo(ZERO) > 0);
	}
	
}
