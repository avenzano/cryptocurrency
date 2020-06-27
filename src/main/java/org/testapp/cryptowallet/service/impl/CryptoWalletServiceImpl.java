package org.testapp.cryptowallet.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.testapp.cryptowallet.dao.CryptoCurrencyDao;
import org.testapp.cryptowallet.dao.CryptoWalletDao;
import org.testapp.cryptowallet.dao.UserDao;
import org.testapp.cryptowallet.exception.InsufficientFundsException;
import org.testapp.cryptowallet.model.BuyOrder;
import org.testapp.cryptowallet.model.CryptoWallet;
import org.testapp.cryptowallet.model.InOrder;
import org.testapp.cryptowallet.model.OutOrder;
import org.testapp.cryptowallet.model.TransferOrder;
import org.testapp.cryptowallet.model.User;
import org.testapp.cryptowallet.service.CryptoWalletService;

@Service
public class CryptoWalletServiceImpl implements CryptoWalletService {

	@Autowired
	private CryptoWalletDao walletDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CryptoCurrencyDao currencyDao;

	@Override
	public CryptoWallet findById(String id) {
		return walletDao.findById(id);
	}
	
	@Override
	@Cacheable("wallets")
	public CryptoWallet getById(String id) {
		return walletDao.getById(id);
	}
	
	@Override
	public CryptoWallet create(CryptoWallet wallet) {
		// TODO validations
		wallet.setId(createUniqueId());
		wallet.setCreated(LocalDateTime.now());
		return walletDao.save(wallet);
	}
	
	private String createUniqueId() {
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

	@Override
	@CachePut("wallets")
	public CryptoWallet update(CryptoWallet wallet) {
		CryptoWallet persisted = walletDao.getById(wallet.getId());
		if ( !persisted.getUsername().equals(wallet.getUsername()) ) {
			throw new IllegalArgumentException("The provided username is invalid: "+wallet.getUsername());
		}
		// this is the only field updateable (id and creation date are generated, 
		// currencies and operations are changed through other REST operations.
		persisted.setFriendlyName(wallet.getFriendlyName());
		return walletDao.update(wallet);
	}
	
	@Override
	@CacheEvict("wallets")
	public CryptoWallet delete(String walletId) {
		CryptoWallet wallet = walletDao.getById(walletId);
		if ( wallet.hasValues() ) {
			throw new IllegalStateException("The wallet has values. It must be emptied before deletion");
		}
		User owner = userDao.getByUsername(wallet.getUsername());
		owner.remove(wallet);
		walletDao.remove(wallet);
		return wallet;
	}

	@Override
	@CacheEvict(value="inOrders", key = "#id")
	public BuyOrder execute(String id, BuyOrder order) {
		CryptoWallet wallet = walletDao.getById(id);
		User owner = userDao.getByUsername(wallet.getUsername());
		// check that the owner has enough money to buy
		BigDecimal srcRatio = currencyDao.getRatio(owner.getAccount().getCurrency().getSymbol(), order.getSourceCurrency().getSymbol());
		BigDecimal buyRatio = currencyDao.getRatio(order.getSourceCurrency().getSymbol(), order.getTargetCurrencySymbol());
		// calculate the amount required to check funds
		BigDecimal amountReq = srcRatio.multiply(order.getSourceAmount());
		if ( owner.getAccount().getBalance().compareTo(amountReq) < 0 ) {
			throw new InsufficientFundsException("You don't have enough funds to execute this buy order (requires "+order.getSourceAmount()+" "+order.getSourceCurrency()+")");
		}
		order.setTargetAmount(amountReq.multiply(buyRatio));
		owner.getAccount().subtract(amountReq);
		BuyOrder processed = wallet.execute(order);
		
		return processed;
	}
	
	@Override
	@CacheEvict(value="outOrders", key = "#id")
	public TransferOrder execute(String id, TransferOrder order) {
		CryptoWallet wallet = walletDao.getById(id);
		// only to check user existence
		userDao.getByUsername(wallet.getUsername());
		
		CryptoWallet tgtWallet = walletDao.getById(order.getTargetWalletId());
		// TODO validate existence!
		// calculate target currency using ratio
		BigDecimal transferRatio = currencyDao.getRatio(order.getSourceCurrencySymbol(), order.getTargetCurrencySymbol());
		order.setTargetAmount(order.getSourceAmount().multiply(transferRatio));
		
		return wallet.execute(order, tgtWallet);
	}

	@Override
	@Cacheable("inOrders")
	public List<InOrder> getInOrdersById(String id) {
		CryptoWallet wallet = getById(id);
		return wallet.getInOrders();
	}
	
	@Override
	@Cacheable("outOrders")
	public List<OutOrder> getOutOrdersById(String id) {
		CryptoWallet wallet = getById(id);
		return wallet.getOutOrders();
	}
}
