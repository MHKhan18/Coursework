package edu.stevens.cs594.chat.domain;


public interface IUserFactory {
	
	public User createUser() throws IUserDao.UserExn;

}
