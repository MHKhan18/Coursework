package edu.stevens.cs594.chat.service.dto;

public class UserDtoFactory {
	
	ObjectFactory factory;
	
	public UserDtoFactory() {
		factory = new ObjectFactory();
	}
	
	public UserDto createUserDto() {
		return factory.createUserDto();
	}

}
