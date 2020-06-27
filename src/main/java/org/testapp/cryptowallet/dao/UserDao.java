package org.testapp.cryptowallet.dao;

import org.testapp.cryptowallet.model.User;

public interface UserDao {

	User save(User usr);
	
	User getByUsername(String username);
	
}
