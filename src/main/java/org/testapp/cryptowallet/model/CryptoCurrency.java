package org.testapp.cryptowallet.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.ApiModel;

@ApiModel
public class CryptoCurrency implements Symbolizable {

	private String id;
	
	private String name;
	
	private String symbol;
	
	private Map<String, BigDecimal> prices = new HashMap<>();

	public CryptoCurrency(String id, String name, String symbol) {
		super();
		this.id = id;
		this.name = name;
		this.symbol = symbol;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	public Map<String, BigDecimal> getPrices() {
		return prices;
	}
	
	public void addPrice(String symbol, BigDecimal price) {
		prices.put(symbol, price);
	}
}
