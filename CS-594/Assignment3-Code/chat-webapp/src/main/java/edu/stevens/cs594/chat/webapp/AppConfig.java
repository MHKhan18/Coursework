package edu.stevens.cs594.chat.webapp;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.PasswordHash;


/*
 * TODO specify custom forms authentication with login page /login.xhtml, login error page /login-error.xhtml
 */

/*
 * TODO define the database connection
 */

/*
 * TODO specify authentication and authorization using a database (see spec for guidance).
 * Set password hashing algorithm parameters to be consistent with MessageService.
 */

@ApplicationScoped
		
public class AppConfig {

}
