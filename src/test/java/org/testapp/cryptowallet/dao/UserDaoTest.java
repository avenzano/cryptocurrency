package org.testapp.cryptowallet.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.testapp.cryptowallet.dao.impl.UserDaoImpl;
import org.testapp.cryptowallet.model.Account;
import org.testapp.cryptowallet.model.CryptoWallet;
import org.testapp.cryptowallet.model.Deposit;
import org.testapp.cryptowallet.model.SimpleCurrency;
import org.testapp.cryptowallet.model.User;

public class UserDaoTest {

	private UserDao dao = new UserDaoImpl();
	
	@Test
	public void testSaveGet() {
		User saved = dao.save(createUser("first", "last", "11222333"));
		assertNotNull(saved);
		User retrieved = dao.getByUsername(saved.getUsername());
		assertNotNull(retrieved);
		
		assertEquals(saved, retrieved);
	}
	
	@Test
	public void testCleanWallets() {
		User usr1 = createUser("first", "last", "11222333");
		usr1.setWallets(new ArrayList<>());
		usr1.getWallets().add(createWallet("myWallet1"));
		dao.save(usr1);
		dao.clearWallets();
		
		assertTrue(usr1.getWallets().isEmpty());
	}
	
	@Test
	public void testCleanAccounts() {
		User usr1 = createUser("first", "last", "11222333");
		usr1.setAccount(new Account(SimpleCurrency.USD, BigDecimal.TEN));
		List<Deposit> deposits = new ArrayList<>();
		deposits.add(new Deposit("1", SimpleCurrency.USD, BigDecimal.TEN));
		usr1.setDeposits(deposits );
		dao.save(usr1);
		dao.clearAccounts();
		
		assertTrue(usr1.getAccount().getBalance().compareTo(BigDecimal.ZERO) == 0);
		assertTrue(usr1.getDeposits().isEmpty());

	}

	private CryptoWallet createWallet(String name) {
		CryptoWallet wallet = new CryptoWallet();
		wallet.setFriendlyName(name);
		return wallet;
	}

	private User createUser(String first, String last, String id) {
		User usr = new User();
		usr.setUsername(first+last);
		usr.setFirstName(first);
		usr.setLastName(last);
		usr.setId(id);
		usr.setMailAdress(usr.getUsername()+"@mail.com");;
		return usr;
	}
	
}
