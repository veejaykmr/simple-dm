package eu.aclement.oauthdemo.security

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

class OauthUserDetails implements UserDetails {
	
	GrantedAuthority[] authorities
	
	String username
	
	String password
	
	boolean accountNonExpired
	
	boolean accountNonLocked

	boolean credentialsNonExpired
	
	boolean enabled	

}
