package edu.stevens.cs594.chat.domain.dao;

import java.util.List;

import edu.stevens.cs594.chat.domain.User;


public interface IUserDao {
	
	public List<User> getUsers();

	public User getUser (String username) throws UserExn;
	
	public void addUser (User user) throws UserExn;
		
	public void deleteUsers ();
	
	public void sync();
	
}
