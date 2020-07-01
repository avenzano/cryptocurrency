package org.testapp.cryptowallet.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.testapp.cryptowallet.dao.impl.CryptoWalletDaoImpl;
import org.testapp.cryptowallet.model.CryptoWallet;

public class CryptoWalletDaoTest {

	private CryptoWalletDao dao = new CryptoWalletDaoImpl();
	
	@Test
	public void testSaveGetFind() {
		CryptoWallet saved = dao.save(createWallet("1", "wallet1", "username"));
		assertNotNull(saved);
		CryptoWallet get1 = dao.getById("1");
		assertNotNull(get1);
		CryptoWallet find1 = dao.findById("1");
		assertNotNull(find1);
		CryptoWallet notFound = dao.findById("2");
		assertNull(notFound);
		assertThrows(
				IllegalArgumentException.class, 
				() -> dao.getById("3"), 
				"IllegalArgumentException expected while retrieving wallet with unknown id!");
	}
	
	@Test
	public void testUpdate() {
		CryptoWallet saved = dao.save(createWallet("1", "wallet1", "username"));
		assertNotNull(saved);
		CryptoWallet wallet = createWallet("1", "wallet2", "username");
		dao.update(wallet);
		CryptoWallet updated = dao.getById("1");
		assertEquals(updated.getFriendlyName(), "wallet2");
		assertThrows(
				IllegalArgumentException.class, 
				() -> dao.update(createWallet("2", "name", "username")), 
				"IllegalArgumentException expected while updating non existent wallet!");
	}
	
	@Test
	public void testDelete() {
		CryptoWallet saved = dao.save(createWallet("1", "wallet1", "username"));
		assertNotNull(saved);
		CryptoWallet toDelete = createWallet("1", "wallet1", "username");
		dao.remove(toDelete);
		CryptoWallet notFound = dao.findById("1");
		assertNull(notFound);
	}
	
	public CryptoWallet createWallet(String id, String name, String username) {
		CryptoWallet cw = new CryptoWallet();
		cw.setId(id);
		cw.setFriendlyName(name);
		cw.setUsername(username);
		cw.setCurrencies(new HashMap<>());
		cw.setInOrders(new ArrayList<>());
		cw.setOutOrders(new ArrayList<>());
		return cw;
	}
	
}
