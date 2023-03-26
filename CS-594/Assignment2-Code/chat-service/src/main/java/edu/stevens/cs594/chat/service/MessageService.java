package edu.stevens.cs594.chat.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.identitystore.PasswordHash;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.time.Instant;

import edu.stevens.cs594.chat.domain.IMessageDao;
import edu.stevens.cs594.chat.domain.IMessageFactory;
import edu.stevens.cs594.chat.domain.IRoleDao;
import edu.stevens.cs594.chat.domain.IRoleFactory;
import edu.stevens.cs594.chat.domain.IUserDao;
import edu.stevens.cs594.chat.domain.IUserDao.UserExn;
import edu.stevens.cs594.chat.domain.IUserFactory;
import edu.stevens.cs594.chat.domain.Message;
import edu.stevens.cs594.chat.domain.MessageFactory;
import edu.stevens.cs594.chat.domain.Role;
import edu.stevens.cs594.chat.domain.RoleFactory;
import edu.stevens.cs594.chat.domain.User;
import edu.stevens.cs594.chat.domain.UserFactory;
import edu.stevens.cs594.chat.service.dto.MessageDto;
import edu.stevens.cs594.chat.service.dto.MessageDtoFactory;
import edu.stevens.cs594.chat.service.dto.RoleDto;
import edu.stevens.cs594.chat.service.dto.RoleDtoFactory;
import edu.stevens.cs594.chat.service.dto.UserDto;
import edu.stevens.cs594.chat.service.dto.UserDtoFactory;
import edu.stevens.cs594.chat.service.OneTimePassword.OtpAuth;
import edu.stevens.cs594.chat.service.messages.Messages;

@RequestScoped
@Transactional
public class MessageService implements IMessageService {
	
	public static final String PASSWORD_HASHING_ALGORITHM = "SHA-256";

	public static final String CHARSET = "UTF-8";
	
	public static final String ISSUER = "Stevens Institute of Technology";
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(MessageService.class.getCanonicalName());

	private IUserFactory userFactory;
	
	private UserDtoFactory userDtoFactory;

	private IRoleFactory roleFactory;
	
	private RoleDtoFactory roleDtoFactory;

	private IMessageFactory messageFactory;
	
	private MessageDtoFactory messageDtoFactory;

	@Inject
	private IUserDao userDao;

	@Inject
	private IRoleDao roleDao;

	@Inject
	private IMessageDao messageDao;

	/**
	 * Default constructor.
	 */
	public MessageService() {
		roleFactory = new RoleFactory();
		userFactory = new UserFactory();
		messageFactory = new MessageFactory();
		userDtoFactory = new UserDtoFactory();
		roleDtoFactory = new RoleDtoFactory();
		messageDtoFactory = new MessageDtoFactory();
	}
	
	/*
	 * Inject a security context for programmatic authentication and authorization
	 */
	@Inject
	private SecurityContext securityContext;
	
	/*
	 * Inject an implementation of the password hash algorithm
	 */
	@Inject
	private PasswordHash passwordHash;

	@PostConstruct
	private void initialize() {
		/*
		 * Here is an example of how to configure properties of the password hash algorithm.
		 * Make sure these are consistent with @DatabaseIdentityStoreDefinition in AppConfig
		 * in the ChatWebApp.
		 */
		Map<String,String> hashParams = new HashMap<String,String>();
		hashParams.put("Pbkdf2PasswordHash.Iterations", "3072");
		hashParams.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
		hashParams.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
		passwordHash.initialize(hashParams);
	}
	
	@Override
	public void clearDatabase() {
		messageDao.deleteMessages();
		userDao.deleteUsers();
		roleDao.deleteRoles();
	}
	
	private String addUser (UserDto dto, OtpAuth otpAuth) throws MessageServiceExn {
		/*
		 * Add user record with hashed password. The secret for the OTP auth should be 
		 * saved in the user record. Return the OTP auth URI, which will be displayed 
		 * as a QR code.
		 * 
		 * The otpAuth may be null (for a test user, for whom we do not do 2FA).
		 */
		
		try {
			String username = dto.getUsername();
			String password = dto.getPassword();
			if (password == null || password.isEmpty()) {
				throw new MessageServiceExn(Messages.admin_user_bad_password);
			}
			
			String hashedPassword = null;
			/*
			 * TODO set the encoded password hash to be set in the database (use passwordHash).
			 */
			hashedPassword = passwordHash.generate(password.toCharArray());

			String secret = null;
			// if not a test user, set secret from otpAuth
			if (otpAuth != null) { 
				secret = otpAuth.getSecretBase32();
			};
			
			User user = userFactory.createUser();
			user.setUsername(username);
			user.setPassword(hashedPassword);
			user.setOtpSecret(secret);
			user.setName(dto.getName());
			userDao.addUser(user);
			
			// TODO add the roles specified in the DTO to the user object (use editUser)
			editUser(dto);
			
			userDao.sync();
	        
	        return (otpAuth != null) ? otpAuth.getKeyUri() : null;        

		} catch (UserExn e) {
			throw new MessageServiceExn(Messages.admin_user_duplicate);
		}
	}

	@Override
	@RolesAllowed("admin")
	public String addUser (UserDto dto) throws MessageServiceExn {
		OtpAuth otpAuth = null;
		/*
		 * TODO Generate OTP authorization (see OneTimePassword)
		 */
		if (dto == null || dto.getUsername() == null || dto.getUsername().isEmpty()){
			throw new MessageServiceExn(Messages.admin_user_none);
		}
		otpAuth = OneTimePassword.generateOtpAuth(dto.getUsername(), ISSUER);
        return addUser(dto, otpAuth);
	}
	
	
	@Override
	@RolesAllowed("admin")
	public String addTestUser (UserDto dto) throws MessageServiceExn {
        return addUser(dto, null);
	}
	
	
	@Override
	@RolesAllowed("admin")
	public void editUser(UserDto dto) {
		try {
			User user = userDao.getUser(dto.getUsername());
			if (user != null) {
				user.setName(dto.getName());
				
				if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
					String hashedPassword = null;
					/*
					 * TODO set the encoded password hash to be set in the database (use passwordHash).
					 */
					hashedPassword = passwordHash.generate(dto.getPassword().toCharArray());
					user.setPassword(hashedPassword);
				}
				
				Collection<Role> toRemove = new ArrayList<Role>();
				for (Role role : user.getRoles()) {
					toRemove.add(role);
				}
				user.getRoles().removeAll(toRemove);
				for (String rolename : dto.getRoles()) {
					Role role = roleDao.getRole(rolename);
					role.addUser(user);
				}
				
				userDao.sync();
				
			}
		} catch (UserExn e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	public List<RoleDto> getRoles() {
		List<Role> roles = roleDao.getRoles();
		List<RoleDto> dtos = new ArrayList<RoleDto>();
		for (Role role : roles) {
			dtos.add(roleDtoFactory.createRoleDto(role));
		}
		return dtos;
	}
	
	@Override
	public void addRole(RoleDto dto) {
		Role role = roleFactory.createRole();
		role.setRoleName(dto.getRolename());
		role.setDescription(dto.getDisplayName());
		roleDao.addRole(role);
	}

	@Override
	public List<UserDto> getUsers() {
		List<User> users = userDao.getUsers();
		List<UserDto> dtos = new ArrayList<UserDto>();
		for (User user : users) {
			dtos.add(userDtoFactory.createUserDto(user));
		}
		return dtos;
	}

	@Override
	public UserDto getUser(String username) throws MessageServiceExn {
		try {
			User user = userDao.getUser(username);
			UserDto dto = userDtoFactory.createUserDto(user);
			return dto;
		} catch (UserExn e) {
			throw new MessageServiceExn(Messages.admin_user_nosuch);
		}
	}
	
	@Override
	public List<MessageDto> getMessages() {
		List<Message> messages = messageDao.getMessages();
		List<MessageDto> dtos = new ArrayList<MessageDto>();
		for (Message user : messages) {
			dtos.add(messageDtoFactory.createMessageDto(user));
		}
		return dtos;
	}

	@Override
	public void addMessage(MessageDto dto) {
		String loggedInUser = null;
		/*
		 * TODO get the username of the logged-in user (use the security context)
		 */
		loggedInUser = securityContext.getCallerPrincipal().getName();

		if (!loggedInUser.equals(dto.getSender())) {
			throw new IllegalStateException("Poster of message is inconsistent with message metadata.");
		}
		Message message = messageFactory.createMessage();
		message.setMessageId(UUID.randomUUID());
		message.setSender(dto.getSender());
		message.setText(dto.getText());
		message.setTimestamp(dto.getTimestamp());
		messageDao.addMessage(message);
	}
	
	@Override
	public void deleteMessage(UUID id) {
		messageDao.deleteMessage(id);
	}

	@Override
	public void checkOtp(String username, Long otpCode) throws MessageServiceExn {
		// Look up user with user DAO and check the supplied OTP code
		try {
			User user = userDao.getUser(username);
			boolean validOtp = false;
			/* 
			 * TODO check that the provided OTP code matches what is in the user record, using current time.
			 * See OneTimePassword.
			 */
			validOtp = OneTimePassword.checkCode(user.getOtpSecret(), otpCode, Instant.now().toEpochMilli());
			if (!validOtp) {
				throw new MessageServiceExn(Messages.login_invalid_code);
			}
		} catch (UserExn e) {
			throw new MessageServiceExn(Messages.admin_user_nosuch);

		}
	}

}