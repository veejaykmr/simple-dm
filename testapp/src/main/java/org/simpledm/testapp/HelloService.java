package org.simpledm.testapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * JAXRS stub class
 * 
 * @author alex
 *
 */
@Path("/helloworld")
public class HelloService {
	
	@GET
	@Produces("text/plain")
	public String sayHello() {
		throw new RuntimeException("Stub method");
	}

}
 