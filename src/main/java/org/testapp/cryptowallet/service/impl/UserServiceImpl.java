package org.testapp.cryptowallet.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.testapp.cryptowallet.dao.CryptoCurrencyDao;
import org.testapp.cryptowallet.dao.CryptoWalletDao;
import org.testapp.cryptowallet.dao.UserDao;
import org.testapp.cryptowallet.model.Account;
import org.testapp.cryptowallet.model.CryptoWallet;
import org.testapp.cryptowallet.model.Deposit;
import org.testapp.cryptowallet.model.SimpleCurrency;
import org.testapp.cryptowallet.model.User;
import org.testapp.cryptowallet.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CryptoCurrencyDao currencyDao;
	
	// required for clearing
	@Autowired
	private CryptoWalletDao walletDao;
	
	@Override
	@Cacheable("users")
	public User get(String username) {
		return userDao.getByUsername(username);
	}

	@Override
	public User signInUser(User newUser) {
		newUser.setJoinDate(LocalDate.now());
		newUser.setDeposits(new ArrayList<>());
		newUser.setWallets(new ArrayList<>());
		newUser.setAccount(new Account(SimpleCurrency.USD, BigDecimal.ZERO));
		return userDao.save(newUser);
	}

	@Override
	@CacheEvict(value = "deposits", key = "#user.username")
	public Deposit deposit(User user, Deposit deposit) {
		validateDeposit(deposit);
		deposit.setConsolidationDate(LocalDateTime.now());
		deposit.setResponsible("accounting@cryptowallet.com");
		addDepositInAccount(user.getAccount(), deposit);
		user.getDeposits().add(deposit);
		return deposit;
	}

	private void addDepositInAccount(Account account, Deposit deposit) {
		BigDecimal amount;
		if ( account.getCurrency() != deposit.getCurrency()) {
			BigDecimal ratio = currencyDao.getRatio(deposit.getCurrency().getSymbol(), account.getCurrency().getSymbol());
			amount = deposit.getAmount().multiply(ratio);
		} else {
			amount = deposit.getAmount();
		}
		account.setBalance(account.getBalance().add(amount));
	}

	/**
	 * Here we should validate that there's a transaction 
	 * matching de deposit (money should come from somewhere :) )
	 * 
	 * @param deposit
	 */
	private void validateDeposit(Deposit deposit) {
	}

	@Override
	@Cacheable("deposits")
	public List<Deposit> getDeposits(String username) {
		User user = get(username);
		return user.getDeposits();
	}

	@Override
	@Cacheable(value="wallets")
	public List<CryptoWallet> getWallets(String username) {
		User user = get(username);
		return user.getWallets();
	}

	@Override
	@CacheEvict(allEntries = true, cacheNames = {"wallets", "users", "deposits", "inOrders", "outOrders"})
	public void clear(String type) {
		if ( "users".equalsIgnoreCase(type) || "all".equalsIgnoreCase(type) ) {
			userDao.clear();
			walletDao.clear();
		}
		if ( "wallets".equalsIgnoreCase(type)) {
			userDao.clearWallets();
			walletDao.clear();
		}
		if ( "accountCurrencies".equalsIgnoreCase(type) || "allCurrencies".equalsIgnoreCase(type) ) {
			userDao.clearAccounts();
		}
		if ( "walletCurrencies".equalsIgnoreCase(type) || "allCurrencies".equalsIgnoreCase(type) ) {
			walletDao.clearValues();
		}
	}

}
