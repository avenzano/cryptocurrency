package org.testapp.cryptowallet.dao;

import org.testapp.cryptowallet.model.CryptoWallet;

public interface CryptoWalletDao {

	CryptoWallet save(CryptoWallet wallet);
	
	CryptoWallet findById(String id);

	CryptoWallet update(CryptoWallet wallet);

	/**
	 * Returns the Wallet with the given id.
	 * 
	 * @param walletId the provided id
	 * @return The requested {@link CryptoWallet}
	 * @throws IllegalArgumentException if there is no wallet
	 */
	CryptoWallet getById(String walletId);

	void remove(CryptoWallet wallet);

	void clear();

	void clearValues();
}
