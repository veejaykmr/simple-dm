package org.simpledm.testapp;

require group: 'org.simpledm', module: 'sdm-cxf', revision: SDM_VERSION
require group: 'org.simpledm', module: 'sdm-camel', revision: SDM_VERSION
		
applicationContext = new org.springframework.context.support.ClassPathXmlApplicationContext("org/simpledm/testapp/application-context.xml")

routes(applicationContext) {
	errorHandler noErrorHandler()
	
	from('cxfrs://bean://rsServer') {
		process new DebugProcessor()
		to 'bean://helloService'
	}
}

def stop() {
	
}
