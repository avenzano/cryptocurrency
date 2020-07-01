package org.testapp.cryptowallet.service;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testapp.cryptowallet.CryptowalletApplication;
import org.testapp.cryptowallet.config.AppConfig;
import org.testapp.cryptowallet.dao.CryptoCurrencyDao;
import org.testapp.cryptowallet.dao.UserDao;
import org.testapp.cryptowallet.exception.MissingCoinDataException;
import org.testapp.cryptowallet.model.Account;
import org.testapp.cryptowallet.model.Deposit;
import org.testapp.cryptowallet.model.SimpleCurrency;
import org.testapp.cryptowallet.model.User;
import org.testapp.cryptowallet.service.impl.UserServiceImpl;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {CryptowalletApplication.class, AppConfig.class})
@TestPropertySource("classpath:/application.properties")
public class UserServiceTest {

	@Mock
	private UserDao userDao;
	
	@Mock
	private CryptoCurrencyDao currencyDao;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@Test
	public void testSignIn() {
		User oldUsr = new User();
		oldUsr.setUsername("username");
		User usr = new User();
		usr.setUsername("username");
		given(userDao.findByUsername(usr.getUsername())).willReturn(oldUsr);
		
		try {
			userService.signInUser(usr);
			fail("Should throw IllegalArgumentException");
		} catch (Exception e) { }
		
		then(userDao)
			.should(never())
			.save(usr);
	}
	
	@Test
	public void testDepositSimple() {
		Account mockAccount = mock(Account.class);
		@SuppressWarnings("unchecked")
		List<Deposit> mockDepList = mock(List.class);
		User usr = createUser("11222333", "first", "last", mockAccount, mockDepList);
		BigDecimal depAmount = BigDecimal.TEN;
		Deposit deposit = new Deposit("1", SimpleCurrency.USD, depAmount);
		
		given(mockAccount.getCurrency()).willReturn(SimpleCurrency.USD);
		userService.deposit(usr, deposit);
		
		then(mockAccount).should(times(1)).add(depAmount);
		then(mockDepList).should(times(1)).add(deposit);
		then(currencyDao).should(never()).getRatio("USD", "USD");
	}
	
	@Test
	public void testDepositWithRatio() {
		Account mockAccount = mock(Account.class);
		@SuppressWarnings("unchecked")
		List<Deposit> mockDepList = mock(List.class);
		User usr = createUser("11222333", "first", "last", mockAccount, mockDepList);
		BigDecimal depAmount = BigDecimal.TEN;
		Deposit deposit = new Deposit("1", SimpleCurrency.EUR, depAmount);
		
		given(mockAccount.getCurrency()).willReturn(SimpleCurrency.USD);
		given(currencyDao.getRatio("EUR", "USD")).willReturn(BigDecimal.ONE);
		userService.deposit(usr, deposit);
		
		then(mockAccount).should(times(1)).add(depAmount);
		then(mockDepList).should(times(1)).add(deposit);
		then(currencyDao).should(times(1)).getRatio("EUR", "USD");
	}
	
	@Test
	public void testDepositFailsNoConversionRatio() {
		Account mockAccount = mock(Account.class);
		@SuppressWarnings("unchecked")
		List<Deposit> mockDepList = mock(List.class);
		User usr = createUser("11222333", "first", "last", mockAccount, mockDepList);
		BigDecimal depAmount = BigDecimal.TEN;
		Deposit deposit = new Deposit("1", SimpleCurrency.EUR, depAmount);
		
		given(mockAccount.getCurrency()).willReturn(SimpleCurrency.USD);
		when(currencyDao.getRatio("EUR", "USD")).thenThrow(new MissingCoinDataException());
		try {
			userService.deposit(usr, deposit);
			fail("A MissingCoinDataException should have been thrown!");
		} catch(MissingCoinDataException mcde) {
			
		}
		then(mockAccount).should(never()).add(depAmount);
		then(mockDepList).should(never()).add(deposit);
		then(currencyDao).should(times(1)).getRatio("EUR", "USD");
	}
	
	
	private User createUser(String id, String fName, String lName, Account account, List<Deposit> deposits) {
		User usr = new User();
		usr.setId(id);
		usr.setFirstName(fName);
		usr.setLastName(lName);
		usr.setAccount(account);
		usr.setDeposits(deposits);
		
		return usr;
	}
	
}
