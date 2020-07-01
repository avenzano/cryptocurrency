package org.testapp.cryptowallet.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testapp.cryptowallet.dao.CryptoWalletDao;
import org.testapp.cryptowallet.model.CryptoWallet;
import org.testapp.cryptowallet.service.WalletIdGeneratorService;

@Service
public class WalletIdGeneratorServiceImpl implements WalletIdGeneratorService {

	@Autowired
	private CryptoWalletDao walletDao;
	
	@Override
	public String generateWalletId() {
		for (int i = 0; i < 3; i++) {
			String uuid = UUID.randomUUID().toString().replace("-", "");
			CryptoWallet wallet = walletDao.findById(uuid);
			if ( wallet == null) {
				return uuid;
			}
		}
		// should never happen, but just in case..
		throw new IllegalStateException("Cannot generate wallet ID: too many UUID collisions");
	}

}
