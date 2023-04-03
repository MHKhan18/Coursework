package edu.stevens.cs594.chat.messages.api;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;


import org.eclipse.microprofile.auth.LoginConfig;

import edu.stevens.cs594.chat.service.dto.RoleDto;

@ApplicationScoped
@ApplicationPath("/")

/*
 * TODO define the database connection
 */
@DataSourceDefinition(
	name="java:app/jdbc/cs594m",
	className="org.postgresql.ds.PGSimpleDataSource",
	user="cs594mess",
	password="${ENV=DATABASE_PASSWORD}",
	databaseName="cs594m",
	serverName="${ENV=DATABASE_HOST}",
	portNumber=5432
)


// TODO activate JWT authentication
@LoginConfig(authMethod = "MP-JWT")
			// realmName = "My-MP-JWT")
			
@DeclareRoles({RoleDto.ROLE_ADMIN, RoleDto.ROLE_MODERATOR, RoleDto.ROLE_POSTER})

public class AppConfig extends Application {
	
	private static final Logger logger = Logger.getLogger(AppConfig.class.getCanonicalName());
	
	@PostConstruct
	private void init() {
		logger.info("Messages Web Service deployed!");
	}

	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(RemoteMessageService.class);
        return s;
    }
}
