package edu.stevens.cs594.chat.service.dto;

import edu.stevens.cs594.chat.domain.Role;
import edu.stevens.cs594.chat.domain.User;

public class UserDtoFactory {

	public UserDto createUserDto() {
		return new UserDto();
	}
	
	public UserDto createUserDto(User u) {
		UserDto d = createUserDto();
		d.setUsername(u.getUsername());
		d.setName(u.getName());
		for (Role role : u.getRoles()) {
			d.getRoles().add(role.getRoleName());
		}
		return d;
	}

}
