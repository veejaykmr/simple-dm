configuration { 
	
   module("eu.aclement:oauthdemo:0.1-SNAPSHOT") {
		dir '/home/alex/projects/oauthdemo/target/classes'
	}

	module("org.sdm:testapp:$SDM_VERSION") {
		dir '/home/alex/projects/simple-dm/testapp/target/classes'
	}	
	
	springOverride {
		override 'org.springframework:spring-support', 'org.springframework:spring-context-support:3.0.3.RELEASE'
		override 'org.springframework:spring-*:3.0.3.RELEASE'
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
	}
	
}