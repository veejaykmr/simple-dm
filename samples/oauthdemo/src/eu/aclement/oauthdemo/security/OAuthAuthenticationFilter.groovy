package eu.aclement.oauthdemo.security

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.ui.FilterChainOrder.*;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.oauth.consumer.token.HttpSessionBasedTokenServicesFactory;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;
import org.springframework.security.ui.SpringSecurityFilter;

class OAuthAuthenticationFilter extends SpringSecurityFilter {
	
	String resourceId
	
	def authenticationProvider
		
	@Override
	protected void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		OAuthConsumerTokenServicesFactory tokenServicesFactory = new HttpSessionBasedTokenServicesFactory();
		def tokenService = tokenServicesFactory.getTokenServices(null, request);
		def token = tokenService.getToken(resourceId)
		
		if (token?.accessToken) {
			def authentication = new OauthUserAuthentication(token)
			def authResult = authenticationProvider.authenticate(authentication)
			SecurityContextHolder.getContext().authentication = authResult
		}
		
		chain.doFilter request, response
	}
	
	public int getOrder() {
		0
	}	
	
	
}
