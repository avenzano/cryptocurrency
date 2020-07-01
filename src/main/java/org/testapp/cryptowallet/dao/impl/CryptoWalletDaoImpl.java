package org.testapp.cryptowallet.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.testapp.cryptowallet.dao.CryptoWalletDao;
import org.testapp.cryptowallet.model.CryptoWallet;

@Component
public class CryptoWalletDaoImpl implements CryptoWalletDao {

	private Map<String, CryptoWallet> cache = new HashMap<>();
	
	@Override
	public CryptoWallet save(CryptoWallet wallet) {
		cache.put(wallet.getId(), wallet);
		return wallet;
	}

	@Override
	public CryptoWallet findById(String id) {
		return cache.get(id);
	}
	
	@Override
	public CryptoWallet getById(String id) {
		if ( ! cache.containsKey(id)) {
			throw new IllegalArgumentException("There is no cryptowallet with id "+id);
		}
		return cache.get(id);
	}

	@Override
	public CryptoWallet update(CryptoWallet wallet) {
		CryptoWallet oldWallet = cache.get(wallet.getId());
		if ( oldWallet == null ) {
			throw new IllegalArgumentException("No wallet with id "+wallet.getId());
		}
		cache.put(wallet.getId(), wallet);
		return wallet;
	}

	@Override
	public void remove(CryptoWallet wallet) {
		cache.remove(wallet.getId());
	}

	@Override
	public void clear() {
		cache.clear();
	}
	
	@Override
	public void clearValues() {
		cache.values().forEach( w -> {
			w.getCurrencies().clear();
		});
	}
	
	
}
