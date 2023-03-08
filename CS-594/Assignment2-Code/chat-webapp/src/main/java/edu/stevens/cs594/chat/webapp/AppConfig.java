package edu.stevens.cs594.chat.webapp;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.enterprise.context.ApplicationScoped;


/*
 * TODO specify custom forms authentication with login page /login.xhtml, login error page /login-error.xhtml
 */

/*
 * TODO specify authentication and authorization using a database (see spec for guidance).
 * Set password hashing algorithm parameters to be consistent with MessageService.
 */

@DataSourceDefinition(
        name="java:global/jdbc/cs594",
        className="org.postgresql.ds.PGSimpleDataSource",
        user="${ENV=DATABASE_USER}",
        password="${ENV=DATABASE_PASSWORD}",
        databaseName="${ENV=DATABASE_NAME}",
        serverName="${ENV=DATABASE_HOST}",
        portNumber=5432)

@ApplicationScoped

public class AppConfig {

}
