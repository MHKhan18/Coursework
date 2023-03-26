package edu.stevens.cs594.chat.service.dto;

public class RoleDtoFactory {
	
	ObjectFactory factory;
	
	public RoleDtoFactory() {
		factory = new ObjectFactory();
	}
	
	public RoleDto createRoleDto() {
		return factory.createRoleDto();
	}

}
