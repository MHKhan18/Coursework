package edu.stevens.cs594.chat.webapp;

import static jakarta.security.enterprise.AuthenticationStatus.SEND_FAILURE;

import edu.stevens.cs594.chat.service.IMessageService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import edu.stevens.cs594.chat.domain.Message;
import edu.stevens.cs594.chat.domain.Role;
import edu.stevens.cs594.chat.service.dto.RoleDto;
import edu.stevens.cs594.chat.service.IMessageService.MessageServiceExn;
import edu.stevens.cs594.chat.service.messages.Messages;

@Named("loginBacking")
@ViewScoped
public class Login extends BaseBacking {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3210700134869332261L;

	private static Logger logger = Logger.getLogger(Login.class.getCanonicalName());
	
	@Inject
	private SecurityContext securityContext;
	
	private String username;

	private String password;

	private String otpCode;

	/*
	 * Note: Jakarta Faces requires that RoleDto be serializable.
	 */
	private List<RoleDto> roles;

	private String selectedRole;

	@Inject
	private IMessageService loginService;

	public String getUsername() {
		if (username == null) {
			Principal prin = securityContext.getCallerPrincipal();
			if (prin != null)
				return prin.getName();
			else
				return null;
		} else {
			return username;
		}
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOtpCode() {
		return otpCode;
	}

	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}

	public String getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(String role) {
		this.selectedRole = role;
	}

	public List<RoleDto> getRoles() {
		return roles;
	}

	@PostConstruct
	public void init() {
		roles = loginService.getRoles();
	}

	public String login() {
		
		HttpServletRequest request = getWebRequest();
		HttpServletResponse response = getWebResponse();
		
		AuthenticationStatus status = null;
		
		// TODO Authenticate using the security context.
		// Use AuthenticationParameters.withParams() to pass credential.
		Credential credential = new UsernamePasswordCredential(username, new Password(password));
		status = securityContext.authenticate(request, response, AuthenticationParameters.withParams().credential(credential));
		// End TODO
		
		logger.info("Result of authentication: " + status);
		
		if (status.equals(SEND_FAILURE)) {
			// Never leave a comment like this in production code!
			logger.info("Failed to authenticate "+username+" with password "+password);
			addMessage(Messages.login_invalid_credentials);
			return null;				
		}

		logger.info("Principal: "+getUsername());
		
		/*
		 * Check the one-time password (OTP) required for 2FA
		 */
		try {
			Long code = null;
			if (otpCode != null && !otpCode.isEmpty()) {
				code = (long) Integer.parseInt(otpCode);
			}
			/*
			 * TODO check the input otp with what is in the user record (see loginService)
			 */
			loginService.checkOtp(username, code);

		} catch (NumberFormatException e) {
			addMessage(Messages.login_malformed_code);
			logout();
			return null;
		} catch (MessageServiceExn me){
			logger.info("Failed 2FA attempt: username" + username + " code: " + otpCode);
			addMessage(Messages.login_invalid_code);
			logout();
			return null;
		}

		logger.info("Selected login role: "+selectedRole);
		
		boolean validRole = false;
		/*
		 * TODO Use the security context to check that the selected role is valid for this user.
		 * this.selectedRole is the role name for the role selected in the form.
		 */
		if (selectedRole != null){
			validRole = securityContext.isCallerInRole(this.selectedRole);
		}
		

		if (!validRole) {
			addMessage(Messages.login_invalid_role);
			logout();
			return null;
		}
		if (Role.ROLE_ADMIN.equals(selectedRole)) {
			return Navigation.ADMIN;
		} else if (Role.ROLE_MODERATOR.equals(selectedRole)) {
			return Navigation.MODERATOR;
		} else if (Role.ROLE_POSTER.equals(selectedRole)) {
			return Navigation.POSTER;
		} else {
			throw new IllegalStateException("Unrecognized selectedRole " + selectedRole);
		}

	}
	
	public void logout() {
		try {
			getWebRequest().logout();
		} catch (ServletException e) {
			throw new IllegalStateException("Problem logging out", e);
		}
	}


	public boolean isLoggedIn() {
		// TODO use security context to check if a user is logged in
		return securityContext.getCallerPrincipal() != null;
	}

}
