package org.testapp.cryptowallet.service;

import java.util.List;

import org.testapp.cryptowallet.model.Deposit;
import org.testapp.cryptowallet.model.User;

public interface UserService {

	User get(String userId);
	
	User signInUser(User newUser);

	/**
	 * Transfer an amount of money in a specified currency to
	 * te User's account.
	 * 
	 * @param user
	 * @param deposit
	 * @return
	 */
	Deposit deposit(User user, Deposit deposit);

	List<Deposit> getDeposits(String id);

}
