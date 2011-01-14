package eu.aclement.oauthdemo.security

import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationServiceException;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.providers.AuthenticationProvider;

import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthParameters;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarFeed;

import org.springframework.security.userdetails.UsernameNotFoundException;

class OauthUserAuthenticationProvider implements AuthenticationProvider {
	
	def consumerKey
	
	def consumerSecret

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if(!supports(authentication.class)) {
			return null
		}
		OAuthConsumerToken token = (OAuthConsumerToken) authentication.credentials
		if (!token) {
			throw new BadCredentialsException("Invalid OAuthConsumerToken")
		}
		
		def details = getUserDetails(token)
		
		def result = new OauthUserAuthentication(details, token, details.authorities)
	}
	
	
	def getUserDetails(token) {
		OAuthParameters params = new OAuthParameters();
		params.setOAuthConsumerKey(consumerKey);
		params.setOAuthConsumerSecret(consumerSecret);
		params.setOAuthToken(token.value);
		params.setOAuthTokenSecret(token.secret);
		
		CalendarFeed resultFeed
		try {
			CalendarService calendarService = new CalendarService("oauthdemo");
			calendarService.setOAuthCredentials(params, new OAuthHmacSha1Signer())
		
			URL feedUrl = new URL("http://www.google.com/calendar/feeds/default/owncalendars/full");
			resultFeed = calendarService.getFeed(feedUrl, CalendarFeed.class);
		} catch(Exception e) {
			throw new AuthenticationServiceException('Service error', e)
		}
		
		def author
		try {
			author = resultFeed.entries[0].authors[0]
		} catch(Exception e) {
			throw new UsernameNotFoundException('Username not found')
		}
		def authorities = [] as GrantedAuthority[]
		
		def details = new OauthUserDetails(authorities: authorities,
				username: author.email, 
				password: "nopassword",
				accountNonExpired: true,
				accountNonLocked: true,
				credentialsNonExpired: true,
				enabled: true)	
	}

	public boolean supports(Class authentication) {
		OauthUserAuthentication.class.isAssignableFrom authentication
	}

}
