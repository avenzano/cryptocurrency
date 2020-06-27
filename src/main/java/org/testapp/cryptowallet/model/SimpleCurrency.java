package org.testapp.cryptowallet.model;

public enum SimpleCurrency implements Symbolizable {

	EUR, USD;

	@Override
	public String getSymbol() {
		return name();
	}
}
