package eu.aclement.oauthdemo.services

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces 

/**
 * 
 * @author alex
 *
 */
@Path("/hello")
class HelloService {
	
	@GET
	@Produces("text/plain")
	String hi() {
		"Hello World!!!"
	}

}
