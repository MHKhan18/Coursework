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
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                errorPage = "/login-error.xhtml")
)

@DataSourceDefinition(
        name="java:global/jdbc/cs594db",
        className="org.postgresql.ds.PGSimpleDataSource",
        user="${ENV=DATABASE_USER}",
        password="${ENV=DATABASE_PASSWORD}",
        databaseName="${ENV=DATABASE_NAME}",
        serverName="${ENV=DATABASE_HOST}",
        portNumber=5432)

/*
 * TODO specify authentication and authorization using a database (see spec for guidance).
 * Set password hashing algorithm parameters to be consistent with MessageService.
 */
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:global/jdbc/cs594db",
        callerQuery = "select PASSWORD from USERS where USERNAME = ?",
        groupsQuery = "select ROLENAME from USERS_ROLES where USERNAME = ?",
        hashAlgorithm = PasswordHash.class,
        priority = 30,
        hashAlgorithmParameters = {
                "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512",
                "Pbkdf2PasswordHash.Iterations=3072",
                "Pbkdf2PasswordHash.SaltSizeBytes=64"
        }
)

@ApplicationScoped

public class AppConfig {

}
