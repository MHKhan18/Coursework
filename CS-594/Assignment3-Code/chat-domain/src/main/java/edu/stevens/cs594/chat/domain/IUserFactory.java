package edu.stevens.cs594.chat.domain;

import edu.stevens.cs594.chat.domain.dao.UserExn;

public interface IUserFactory {
	
	public User createUser(String username, String password, String otpSecret, String name) throws UserExn;

}
