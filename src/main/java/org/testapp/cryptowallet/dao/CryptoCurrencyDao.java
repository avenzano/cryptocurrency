package org.testapp.cryptowallet.dao;

import java.math.BigDecimal;
import java.util.List;

import org.testapp.cryptowallet.model.CryptoCurrency;
import org.testapp.cryptowallet.model.Symbolizable;
import org.testapp.cryptowallet.pagination.Page;

public interface CryptoCurrencyDao {

	List<CryptoCurrency> getAll1(int offset, int size);

	BigDecimal getRatio(String symbol1, String symbol2);

	Page<CryptoCurrency> getAll(int offset, int size);
	
}
