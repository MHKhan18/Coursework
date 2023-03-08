package edu.stevens.cs594.chat.domain;

import java.util.List;


public interface IRoleDao {
	
	public List<Role> getRoles();

	public Role getRole (String rolename);
	
	public void addRole (Role role);
	
	public void deleteRoles ();
	
}
