package org.testapp.cryptowallet.service;

import java.util.List;

import org.testapp.cryptowallet.model.BuyOrder;
import org.testapp.cryptowallet.model.CryptoWallet;
import org.testapp.cryptowallet.model.InOrder;
import org.testapp.cryptowallet.model.OutOrder;
import org.testapp.cryptowallet.model.TransferOrder;

public interface CryptoWalletService {

	CryptoWallet findById(String id);

	CryptoWallet create(CryptoWallet newWallet);

	CryptoWallet update(CryptoWallet wallet);
	
	CryptoWallet delete(String walletId);

	BuyOrder execute(String id, BuyOrder order);

	TransferOrder execute(String walltId, TransferOrder order);

	CryptoWallet getById(String id);

	List<InOrder> getInOrdersById(String id);

	List<OutOrder> getOutOrdersById(String id);

	
}
