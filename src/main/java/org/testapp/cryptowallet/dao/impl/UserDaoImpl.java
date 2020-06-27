package org.testapp.cryptowallet.dao.impl;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.testapp.cryptowallet.dao.UserDao;
import org.testapp.cryptowallet.model.User;

@Component
public class UserDaoImpl implements UserDao {

	private Map<String, User> usersByUsername = new HashMap<>();

	@Override
	public User save(User usr) {
		validateSave(usr);
		usersByUsername.put(usr.getUsername(), usr);
		return usr;
	}

	private void validateSave(User usr) {
		if ( isEmpty(usr.getUsername()) ) {
			throw new IllegalArgumentException("A non-emtpy username must be provided");
		}
		if ( usr.getUsername().contains("@") ) {
			throw new IllegalArgumentException("The character '@' cannot be part of the username");
		}
		if ( isEmpty(usr.getFirstName()) || isEmpty(usr.getLastName()) ) {
			throw new IllegalArgumentException("Fist name and last name are mandatory");
		}
		if ( isEmpty(usr.getMailAdress()) ) {
			throw new IllegalArgumentException("A non-emtpy mail address must be provided");
		}
		// TODO check mail address format!
		if ( usersByUsername.containsKey(usr.getUsername()) ) {
			throw new IllegalArgumentException("The selected username is in use");
		}
	}

	@Override
	public User getByUsername(String username) {
		User user = usersByUsername.get(username);
		if ( user == null ) {
			throw new IllegalArgumentException("There is no user with username '"+username+"'");
		}
		return user;
	}
	
}
