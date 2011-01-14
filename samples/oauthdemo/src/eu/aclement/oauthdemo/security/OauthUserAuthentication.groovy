package eu.aclement.oauthdemo.security

import org.springframework.security.GrantedAuthority;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.providers.AbstractAuthenticationToken;

class OauthUserAuthentication extends AbstractAuthenticationToken {
	
	def principal
	
	OAuthConsumerToken credentials	
	
	OauthUserAuthentication(OAuthConsumerToken credentials) {
		super(null)
		this.credentials = credentials
	}
	
	OauthUserAuthentication(principal, OAuthConsumerToken credentials, GrantedAuthority[] authorities) {
		super(authorities)
		this.principal = principal
		this.credentials = credentials
		authenticated = true
	}

	Object getPrincipal() {
		principal
	}
	
	Object getCredentials() {
		credentials
	}

}
