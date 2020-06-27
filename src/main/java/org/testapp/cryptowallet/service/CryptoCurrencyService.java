package org.testapp.cryptowallet.service;

import java.math.BigDecimal;
import java.util.List;

import org.testapp.cryptowallet.model.CryptoCurrency;
import org.testapp.cryptowallet.pagination.Page;

public interface CryptoCurrencyService {

	/**
	 * Return the actual exchange ratio between source and target
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	BigDecimal getRatio(CryptoCurrency source, CryptoCurrency target);
	
	/**
	 * Get all available currencies
	 * 
	 * @return
	 */
	List<CryptoCurrency> getAvailableCurrencies(int offset, int size);
	Page<CryptoCurrency> getAvailableCurrencies2(int offset, int size);
}
