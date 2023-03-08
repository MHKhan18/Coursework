package edu.stevens.cs594.chat.service.dto;

import edu.stevens.cs594.chat.domain.Role;

public class RoleDtoFactory {

	public RoleDto createRoleDto() {
		return new RoleDto();
	}
	
	public RoleDto createRoleDto(Role r) {
		RoleDto d = createRoleDto();
		d.setRolename(r.getRoleName());
		d.setDisplayName(r.getDescription());
		return d;
	}

}
