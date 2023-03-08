package edu.stevens.cs594.chat.domain;


public class UserFactory implements IUserFactory {

	@Override
	public User createUser() throws IUserDao.UserExn {
		return new User();
	}
	
}
