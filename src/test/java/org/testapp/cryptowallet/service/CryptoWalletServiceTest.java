package org.testapp.cryptowallet.service;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testapp.cryptowallet.CryptowalletApplication;
import org.testapp.cryptowallet.config.AppConfig;
import org.testapp.cryptowallet.dao.CryptoCurrencyDao;
import org.testapp.cryptowallet.dao.CryptoWalletDao;
import org.testapp.cryptowallet.dao.UserDao;
import org.testapp.cryptowallet.model.CryptoWallet;
import org.testapp.cryptowallet.model.User;
import org.testapp.cryptowallet.service.impl.CryptoWalletServiceImpl;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {CryptowalletApplication.class, AppConfig.class})
@TestPropertySource("classpath:/application.properties")
public class CryptoWalletServiceTest {

	@Mock
	private CryptoWalletDao walletDao;
	
	@Mock
	private UserDao userDao;

	@Mock
	private CryptoCurrencyDao currencyDao;
	
	@Mock
	private WalletIdGeneratorService idGeneratorSrv;
	
	@InjectMocks
	private CryptoWalletServiceImpl walletService;
	
	@Test
	public void testWalletCreation() {
		
		CryptoWallet wallet = mock(CryptoWallet.class);
		User mockUser = mock(User.class);
		
		when(wallet.getUsername()).thenReturn("username");
		when(userDao.getByUsername("username")).thenReturn(mockUser);
		when(idGeneratorSrv.generateWalletId()).thenReturn("1");
		
		walletService.create(wallet);
		
		verify(wallet, times(1)).setId("1");
		verify(mockUser, times(1)).addWallet(wallet);
		verify(walletDao, times(1)).save(wallet);
	}
	
	@Test
	public void testWalletUpdateFailsUsername() {
		CryptoWallet wrongWallet = mock(CryptoWallet.class);
		
		CryptoWallet wallet = mock(CryptoWallet.class);
		
		when(wrongWallet.getUsername()).thenReturn("username2");
		when(wallet.getId()).thenReturn("1");
		when(wallet.getUsername()).thenReturn("username");
		when(walletDao.getById("1")).thenReturn(wrongWallet);
		try {
			walletService.update(wallet);
			fail("Should have failed with IllegalArgumentException!");
		} catch(IllegalArgumentException e) {}
	}
	
	@Test
	public void testWalletUpdateSucceeds() {
		String newFriendlyName = "new Friendly Name";
		CryptoWallet wallet = mock(CryptoWallet.class);
		// username validation passes
		when(wallet.getId()).thenReturn("1");
		when(wallet.getUsername()).thenReturn("username");
		when(walletDao.getById("1")).thenReturn(wallet);
		when(wallet.getFriendlyName()).thenReturn(newFriendlyName);
		
		walletService.update(wallet);
		// only friendly name is called
		verify(wallet, never()).setId(Mockito.anyString());
		verify(wallet, times(1)).setFriendlyName(Mockito.anyString());
		verify(wallet, never()).setUsername(Mockito.anyString());
	}
	
	@Test
	public void testWalletDelete() {
		String id = "1";
		CryptoWallet wallet = mock(CryptoWallet.class);
		User user = mock(User.class);
		
		when(wallet.getUsername()).thenReturn("username");
		when(wallet.hasValues()).thenReturn(false);
		when(walletDao.getById(id)).thenReturn(wallet);
		when(userDao.getByUsername("username")).thenReturn(user);
		
		walletService.delete(id);
		
		verify(user, times(1)).remove(wallet);
		verify(walletDao, times(1)).remove(wallet);
	}
	
	@Test
	public void testWalletDeleteFailsHasCurrencies() {
		String id = "1";
		CryptoWallet wallet = mock(CryptoWallet.class);
		
		when(walletDao.getById(id)).thenReturn(wallet);
		when(wallet.hasValues()).thenReturn(true);
		
		try {
			walletService.delete(id);
			fail("An IllegalStateException should have been thrown!");
		} catch(IllegalStateException e) {}
		
		verify(walletDao, never()).remove(wallet);
		verify(userDao, never()).getByUsername(Mockito.anyString());
	}
	
	@Test
	public void testWalletDeleteFailsWrongUser() {
		String id = "1";
		String username = "username";
		CryptoWallet wallet = mock(CryptoWallet.class);
		User user = mock(User.class);
		
		when(walletDao.getById(id)).thenReturn(wallet);
		when(wallet.getUsername()).thenReturn(username);
		when(wallet.hasValues()).thenReturn(false);
		when(userDao.getByUsername(username)).thenThrow(new IllegalArgumentException());
		
		try {
			walletService.delete(id);
		} catch(IllegalArgumentException e) {}
		
		verify(walletDao, never()).remove(wallet);
		verify(user, never()).remove(wallet);
	}
	
	
}
