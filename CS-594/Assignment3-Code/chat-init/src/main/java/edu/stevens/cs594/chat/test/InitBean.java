package edu.stevens.cs594.chat.test;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import java.util.logging.Logger;

import edu.stevens.cs594.chat.service.IMessageService;
import edu.stevens.cs594.chat.service.IMessageService.MessageServiceExn;
import edu.stevens.cs594.chat.service.dto.RoleDto;
import edu.stevens.cs594.chat.service.dto.RoleDtoFactory;
import edu.stevens.cs594.chat.service.dto.UserDto;
import edu.stevens.cs594.chat.service.dto.UserDtoFactory;



/**
 * Session Bean implementation class TestBean
 */
@Singleton
@LocalBean
@Startup
@RunAs("admin")
// @ApplicationScoped
public class InitBean {
	
	public static final String ADMINISTRATOR = RoleDto.ROLE_ADMIN;;
	public static final String MODERATOR = RoleDto.ROLE_MODERATOR;
	public static final String POSTER = RoleDto.ROLE_POSTER;
	
	private static Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());

	/**
	 * Default constructor.
	 */
	public InitBean() {
	}
	
	@Inject
	private IMessageService messageService;
	
	@PostConstruct
	private void init() {
		
		logger.info("Mohammad Khan: ");

		messageService.clearDatabase();
		
		UserDtoFactory userDtoFactory = new UserDtoFactory();

		RoleDtoFactory roleDtoFactory = new RoleDtoFactory();
		
		/*
		 * Put your initialization logic here. Use the logger to display testing output in the server logs.
		 * Make sure to initialize the roles database!
		 * Use the list of roles specified in the Role entity class.
		 */

		try {
			
			for (int ix=0; ix < RoleDto.INIT_ROLE_NAMES.length; ix++) {
				RoleDto role = roleDtoFactory.createRoleDto();
				role.setRolename(RoleDto.INIT_ROLE_NAMES[ix]);
				role.setDisplayName(RoleDto.INIT_ROLE_DISPLAY_NAMES[ix]);
				messageService.addRole(role);
			}

			// Map the admin role to this principal for initialization (@RunAs)
			UserDto admin = userDtoFactory.createUserDto();
			admin.setUsername("admin");
			admin.setPassword("abc123");
			admin.setName("Administrator");
			admin.getRoles().add(ADMINISTRATOR);
			logger.info("Adding user admin");
			messageService.addTestUser(admin);
			
			UserDto joe = userDtoFactory.createUserDto();
			joe.setUsername("joe");
			joe.setPassword("abc123");
			joe.setName("Joe Smith");
			joe.getRoles().add(ADMINISTRATOR);
			logger.info("Adding user joe");
			messageService.addTestUser(joe);
			
			UserDto jane = userDtoFactory.createUserDto();
			jane.setUsername("jane");
			jane.setPassword("xyz789");
			jane.setName("Jane Doe");
			jane.getRoles().add(MODERATOR);
			jane.getRoles().add(POSTER);
			logger.info("Adding user jane");
			messageService.addTestUser(jane);
			
			UserDto john = userDtoFactory.createUserDto();
			john.setUsername("john");
			john.setPassword("foobar!");
			john.setName("John Doe");
			john.getRoles().add(POSTER);
			logger.info("Adding user john");
			messageService.addTestUser(john);
			
			// TODO add more testing

			UserDto jack = userDtoFactory.createUserDto();
			jack.setUsername("jack");
			jack.setPassword("foobar!");
			jack.setName("Jack Ross");
			jack.getRoles().add(MODERATOR);
			logger.info("Adding user jack");
			messageService.addTestUser(jack);
			
			
		} catch (MessageServiceExn e) {
			throw new IllegalStateException("Failed to add user record.", e);
		} 
			
	}
	
}
