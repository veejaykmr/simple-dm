package eu.aclement.oauthdemo.security

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.ui.SpringSecurityFilter;

class DebugFilter extends SpringSecurityFilter {
	
	@Override
	protected void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
	
		try {	
			chain.doFilter request, response
		} catch(Throwable e) {
		System.err.println('>>>>>BEGIN stack trace')
		while(e) {
			System.err.println(e)
			e.stackTrace.each { System.err.println it }
			e = e.cause
		}
		System.err.println('>>>>>END stack trace')
		}
	}
	
	public int getOrder() {
		0
	}
}
