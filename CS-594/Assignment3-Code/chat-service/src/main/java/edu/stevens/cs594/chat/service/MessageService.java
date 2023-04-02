package edu.stevens.cs594.chat.service;

import edu.stevens.cs594.chat.domain.IRoleFactory;
import edu.stevens.cs594.chat.domain.IUserFactory;
import edu.stevens.cs594.chat.domain.Role;
import edu.stevens.cs594.chat.domain.RoleFactory;
import edu.stevens.cs594.chat.domain.User;
import edu.stevens.cs594.chat.domain.UserFactory;
import edu.stevens.cs594.chat.domain.dao.IRoleDao;
import edu.stevens.cs594.chat.domain.dao.IUserDao;
import edu.stevens.cs594.chat.domain.dao.UserExn;
import edu.stevens.cs594.chat.messages.restclient.IRemoteMessageService;
import edu.stevens.cs594.chat.service.OneTimePassword.OtpAuth;
import edu.stevens.cs594.chat.service.dto.MessageDto;
import edu.stevens.cs594.chat.service.dto.RoleDto;
import edu.stevens.cs594.chat.service.dto.RoleDtoFactory;
import edu.stevens.cs594.chat.service.dto.UserDto;
import edu.stevens.cs594.chat.service.dto.UserDtoFactory;
import edu.stevens.cs594.chat.service.messages.Messages;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.identitystore.PasswordHash;
import jakarta.transaction.Transactional;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 * Session Bean implementation class UserService
 */
@RequestScoped
@Transactional
public class MessageService implements IMessageService {
	
	/*
	 * Signing requests to microservice.
	 */
	
	public static final String JWT_ISSUER = "edu.stevens.cs594";
	public static final String AUTHORIZATION_HEADER_PREFIX = "Authorization: BEARER";

	public static final String REQUEST_SIGNING_KEY_FILENAME = "META-INF/privateKey.pem";
	
	// Passed as an environment variable
	public static final String REQUEST_SIGNING_KEY_PASSWORD = "KEY_PASSWORD";

	
	public static final String REQUEST_SIGNING_ALGORITHM = "RSA";
	
	public static final String ADMIN_USER_DEFAULT = "admin";
	
	
	/*
	 * Password Hashing.
	 */
	
	public static final String PASSWORD_HASHING_ALGORITHM = "SHA-256";

	public static final String CHARSET = "UTF-8";
	
	/*
	 * TOTP parameters.
	 */

	public static final String ISSUER = "Stevens Institute of Technology";

	private Logger logger = Logger.getLogger(MessageService.class.getCanonicalName());

	private IUserFactory userFactory;

	private UserDtoFactory userDtoFactory;

	private IRoleFactory roleFactory;

	private RoleDtoFactory roleDtoFactory;

	/**
	 * Default constructor.
	 */
	public MessageService() {
		roleFactory = new RoleFactory();
		userFactory = new UserFactory();
		userDtoFactory = new UserDtoFactory();
		roleDtoFactory = new RoleDtoFactory();
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

	/*
	 * Inject an entity manager to interface with the database
	 */
	@Inject
	private IUserDao userDao;

	@Inject
	private IRoleDao roleDao;

	/*
	 * Inject a client for the remote messaging service, that stores messages.
	 */
	@Inject
	private IRemoteMessageService remoteMessageService;
	
	/*
	 * The secret key used to sign requests to the microservice.
	 */
	private PrivateKey authorizationKey;

	@PostConstruct
	private void initialize() {

		/*
		 * Here is an example of how to configure properties of the password hash
		 * algorithm. Make sure these are consistent
		 * with @DatabaseIdentityStoreDefinition in AppConfig in the ChatWebApp.
		 */
		Map<String, String> hashParams = new HashMap<String, String>();
		hashParams.put("Pbkdf2PasswordHash.Iterations", "3072");
		hashParams.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
		hashParams.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
		passwordHash.initialize(hashParams);
		
		/*
		 * Init the signing key for microservice requests
		 */
		String keyPassword = System.getenv(REQUEST_SIGNING_KEY_PASSWORD);
		if (keyPassword == null) {
			throw new IllegalArgumentException("Undefined environment variable: " + REQUEST_SIGNING_KEY_PASSWORD);
		}
		authorizationKey = readPrivateKey(keyPassword.toCharArray());
	}
	
	/*
	 * Read the private key, for signing requests to microservice, from a file resource.
	 */
    private PrivateKey readPrivateKey(char[] keyPassword) {
    	try {
    		/*
    		 * Read the private key from a PEM file into a byte buffer
    		 */
    		byte[] buffer = new byte[16384];
        
    		int length = getClass().getClassLoader()
        		               	   .getResourceAsStream(REQUEST_SIGNING_KEY_FILENAME)
        		               	   .read(buffer);

    		String key = new String(buffer, 0, length).replaceAll("-----BEGIN (.*)-----", "")
                                                      .replaceAll("-----END (.*)----", "")
                                                      .replaceAll("\r\n", "")
                                                      .replaceAll("\n", "")
                                                      .trim();
    		byte[] keyBytes = Base64.getDecoder().decode(key);

			logger.log(Level.INFO, "inside read pricate key" + key);
    		
    		/*
    		 * Decrypt a password-protected private key (password should be defined as env variable).
    		 * https://stackoverflow.com/a/2661495
    		 * 
    		 * First, get the secret encryption key from the password.
    		 */
    		EncryptedPrivateKeyInfo ePKInfo = new EncryptedPrivateKeyInfo(keyBytes);    		
    		SecretKeyFactory fac = SecretKeyFactory.getInstance(getKeyEncryptionAlg(ePKInfo));
    		PBEKeySpec pbeKeySpec = new PBEKeySpec(keyPassword);
    		SecretKey pbeKey = fac.generateSecret(pbeKeySpec);
    		
    		/*
    		 * Now decrypt the signing key
    		 */
    		Cipher cipher = Cipher.getInstance(getKeyEncryptionAlg(ePKInfo));
            cipher.init(Cipher.DECRYPT_MODE, pbeKey, ePKInfo.getAlgParameters());
            PKCS8EncodedKeySpec pkcs8KeySpec = ePKInfo.getKeySpec(cipher);
            
    		/*
    		 * Finally, generate the private key from the decrypted spec.
    		 */
    		return KeyFactory.getInstance(REQUEST_SIGNING_ALGORITHM)
                             .generatePrivate(pkcs8KeySpec);
    	} catch (Exception e) {
    		throw new IllegalStateException("Failed to read signing key for microservice requests!", e);
    	}
    }
    
    private String getKeyEncryptionAlg(EncryptedPrivateKeyInfo ePKInfo) {
    	String algName = ePKInfo.getAlgName();
    	logger.info("Key encryption algorithm: "+algName);
    	return algName;
    }
	

	/**
	 * Create a JWT token for authenticating requests to messaging microservice.
	 * @param signingKey
	 * 	Key for signing requests.
	 * @param username
	 * 	Principal issuing the request.
	 * @param groups
	 * 	Role(s) being claimed for the request.
	 * @return
	 * 	JWT token
	 */
	private String createToken(PrivateKey signingKey, String username, Set<String> groups) {
		long now = (new Date()).getTime();
		long validity = TimeUnit.HOURS.toMillis(10);

		String token = null;

		/*
		 * TODO return the JWT string (include all required claims for MP-JWT)
		 * 
		 * See lectures for format of JWT Authorization header
		 */
		token = Jwts.builder()	
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("alg", "RS256")
				// .setHeaderParam("kid", "abc-1234567890")
				.setIssuer(JWT_ISSUER)
				.setSubject(username)
				.setAudience("chat-webapp")
				.setExpiration(new Date(now + validity))
				.setIssuedAt(new Date(now))
				.setId(UUID.randomUUID().toString())
				.claim("groups", groups)
				.claim("upn", username)
				.signWith(signingKey)
				.compact();

		logger.info("JWT: "+token);

		return token;
	}
		
	/**
	 * HTTP authorization header for logged-in user requests.
	 */
	private String createLoggedInAuthorizationHeader() {
		/*
		 * TODO return the Authorization header field.
		 * 
		 * Use the security context to fill in username and groups.
		 */
		Set<String> groups = new HashSet<String>();
		for(String role: RoleDto.INIT_ROLE_NAMES){
			if (securityContext.isCallerInRole(role)){
				groups.add(role);
			}
		}
		return createAuthorizationHeader(securityContext.getCallerPrincipal().getName(), groups);
	}
	
	/**
	 * HTTP authorization header for administrative requests (during initialization).
	 */
	private String createAdminAuthorizationHeader() {
		Set<String> groups = new HashSet<String>();
		groups.add(RoleDto.ROLE_ADMIN);
		return createAuthorizationHeader(ADMIN_USER_DEFAULT, groups);
	}
	
	/**
	 * Create authorizaiton header with the JWT token.
	 */
	private String createAuthorizationHeader(String username, Set<String> groups) {
		// TODO complete this
		String jwt = createToken(authorizationKey, username, groups);
		return AUTHORIZATION_HEADER_PREFIX + " " + jwt;

	}
	
	@Override
	public void clearDatabase() {
		remoteMessageService.deleteMessages(createAdminAuthorizationHeader());
		
		userDao.deleteUsers();
		roleDao.deleteRoles();
	}

	private String addUser(UserDto userDto, OtpAuth otpAuth) throws MessageServiceExn {
		/*
		 * Add user record with hashed password. The secret for the OTP auth should be
		 * saved in the user record. Return the OTP auth URI, which will be displayed as
		 * a QR code.
		 * 
		 * The otpAuth may be null (for a test user, for whom we do not do 2FA).
		 */

		try {
			String password = userDto.getPassword();
			if (password == null || password.isEmpty()) {
				throw new MessageServiceExn(Messages.admin_user_bad_password);
			}

			String hashedPassword = null;
			/*
			 * TODO set the encoded password hash to be set in the database (use
			 * passwordHash).
			 */
			hashedPassword = passwordHash.generate(password.toCharArray());


			String secret = null;
			// if not a test user, set secret from otpAuth
			if (otpAuth != null) {
				secret = otpAuth.getSecretBase32();
			}
			;

			String username = userDto.getUsername();
			String name = userDto.getName();
			User user = userFactory.createUser(username, hashedPassword, secret, name);
			userDao.addUser(user);

			// TODO Add the roles specified in the DTO to the user object (use editUser)
			editUser(userDto);


			userDao.sync();

			return (otpAuth != null) ? otpAuth.getKeyUri() : null;

		} catch (UserExn e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new MessageServiceExn(Messages.admin_user_duplicate);
		}
	}

	@Override
	@RolesAllowed("admin")
	public String addUser(UserDto dto) throws MessageServiceExn {
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
	public String addTestUser(UserDto dto) throws MessageServiceExn {
		return addUser(dto, null);
	}

	@Override
	public void editUser(UserDto dto) {
		try {
			User user = userDao.getUser(dto.getUsername());
			if (user != null) {
				user.setName(dto.getName());

				if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
					String hashedPassword = null;
					/*
					 * TODO set the encoded password hash to be set in the database (use
					 * passwordHash).
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
			RoleDto roleDto = roleDtoFactory.createRoleDto();
			roleDto.setRolename(role.getRoleName());
			roleDto.setDisplayName(role.getDescription());
			dtos.add(roleDto);
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
			UserDto userDto = userDtoFactory.createUserDto();
			userDto.setUsername(user.getUsername());
			userDto.setName(user.getName());
			dtos.add(userDto);
		}
		return dtos;
	}

	@Override
	public UserDto getUser(String username) throws MessageServiceExn {
		try {
			User user = userDao.getUser(username);
			UserDto dto = userDtoFactory.createUserDto();
			dto.setUsername(user.getUsername());
			dto.setName(user.getName());
			for (Role role : user.getRoles()) {
				dto.getRoles().add(role.getRoleName());
			}
			return dto;
		} catch (UserExn e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new MessageServiceExn(Messages.admin_user_nosuch);
		}
	}

	@Override
	public List<MessageDto> getMessages() {
		return remoteMessageService.getMessages(createLoggedInAuthorizationHeader());
	}

	@Override
	public void addMessage(MessageDto message) {
		remoteMessageService.addMessage(createLoggedInAuthorizationHeader(), message);
	}

	@Override
	public void deleteMessage(UUID id) {
		remoteMessageService.deleteMessage(createLoggedInAuthorizationHeader(), id);
	}

	@Override
	public void checkOtp(String username, Long otpCode) throws MessageServiceExn {
		// Look up user with user DAO and check the supplied OTP code
		try {
			User user = userDao.getUser(username);
			boolean validOtp = false;
			/*
			 * TODO check that the provided OTP code matches what is in the user record,
			 * using current time. See OneTimePassword.
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