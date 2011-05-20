configuration { 
		
	springOverride {
		override 'org.springframework:spring-support', 'org.springframework:spring-context-support:3.0.3.RELEASE'
		override 'org.springframework:spring-*:3.0.3.RELEASE'
	}
	
	commonsLoggingOverride {
		override 'org.slf4j:jcl-over-slf4j', 'commons-logging:commons-logging:1.1.1'
		override 'commons-logging:commons-logging-api', 'commons-logging:commons-logging:1.1.1'		
		override 'commons-logging:commons-logging:1.1.1'
	}
	
	module('org.mortbay.jetty:jetty:6.1.21') {
		override 'org.mortbay.jetty:servlet-api:2.5-20081211', 'org.apache.geronimo.specs:geronimo-servlet_2.5_spec:1.2'
	}
	
	module('org.codehaus.spring-security-oauth:spring-security-oauth:3.18-SNAPSHOT') {
		springOverride()
	}
	
	module('org.springframework.security:spring-security-core:2.0.3') {
		springOverride()
	}		
	
	module('org.grails:grails-spring:1.2.4') {
		override 'org.slf4j:jcl-over-slf4j', 'commons-logging:commons-logging:1.1.1'
	}
	
	module('org.apache.cxf:cxf-*:2.2.6') {
		springOverride()
	}
	
	module('org.apache.camel:camel-*:2.2.0') {
		springOverride()
		commonsLoggingOverride()
	}
	
	module('commons-httpclient:commons-httpclient:3.*') {
		commonsLoggingOverride()
	}
	
}