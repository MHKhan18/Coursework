package edu.stevens.cs594.chat.domain.dao;

import java.util.List;

import edu.stevens.cs594.chat.domain.Role;


public interface IRoleDao {
	
	public List<Role> getRoles();

	public Role getRole (String rolename);
	
	public void addRole (Role role);
	
	public void deleteRoles ();
	
}
