package org.testapp.cryptowallet.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testapp.cryptowallet.dao.CryptoCurrencyDao;
import org.testapp.cryptowallet.model.CryptoCurrency;
import org.testapp.cryptowallet.pagination.Page;
import org.testapp.cryptowallet.service.CryptoCurrencyService;

@Service
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {

	@Autowired
	private CryptoCurrencyDao cryptoCurrencyDao;
	
	@Override
	public BigDecimal getRatio(CryptoCurrency source, CryptoCurrency target) {
		return null;
	}

	@Override
	public List<CryptoCurrency> getAvailableCurrencies(int offset, int size) {
		return cryptoCurrencyDao.getAll1(offset, size);
	}

	@Override
	public Page<CryptoCurrency> getAvailableCurrencies2(int offset, int size) {
		return cryptoCurrencyDao.getAll(offset, size);
	}
	
}
