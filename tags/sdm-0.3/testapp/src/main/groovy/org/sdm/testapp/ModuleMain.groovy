package org.sdm.testapp;

override 'org.mortbay.jetty:servlet-api:2.5-20081211', [group: 'org.apache.geronimo.specs', module:'geronimo-servlet_2.5_spec', revision: '1.2']
override 'commons-logging:commons-logging:1.0.4', [group: 'commons-logging', module: 'commons-logging', revision: '1.1.1']
override 'org.apache.geronimo.specs:geronimo-servlet_2.4_spec:1.1.1', [group: 'org.apache.geronimo.specs', module:'geronimo-servlet_2.5_spec', revision: '1.2']

require group: 'org.sdm', module: 'cxf', revision: SDM_VERSION
require group: 'org.sdm', module: 'camel', revision: SDM_VERSION
		
applicationContext = new org.springframework.context.support.ClassPathXmlApplicationContext("org/sdm/testapp/application-context.xml")
camelContext.applicationContext = applicationContext

routes {
	errorHandler noErrorHandler()
	
	from('cxfrs://bean://rsServer') {
		process new DebugProcessor()
		to 'bean://helloService'
	}
}

camelContext.start()

def stop() {
	
}
