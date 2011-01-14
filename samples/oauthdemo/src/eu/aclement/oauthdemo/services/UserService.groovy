package eu.aclement.oauthdemo.services

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.DefaultValue;

import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

/**
 * 
 * @author alex
 *
 */
@Path("/users")
class UserService {
	
	@GET
	@Produces("application/json")
	User getCaller() {
		def authentication = SecurityContextHolder.getContext().authentication
		def principal = authentication.principal
		def username = principal instanceof UserDetails ? principal.username : principal
		new User(username: username)
	}
	
	@DELETE
	void logout(@Context HttpServletRequest request) {
		request.getSession()?.invalidate()
	}

}
