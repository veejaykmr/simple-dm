package org.sdm.testapp;

require group: 'org.sdm.cxf', module: 'org-sdm-cxf', revision: SDM_VERSION
require group: 'org.sdm.camel', module: 'org-sdm-camel', revision: SDM_VERSION
		
applicationContext = new org.springframework.context.support.ClassPathXmlApplicationContext("org/sdm/testapp/application-context.xml")

routes(applicationContext) {
	errorHandler noErrorHandler()
	
	from('cxfrs://bean://rsServer') {
		process new DebugProcessor()
		to 'bean://helloService'
	}
}

def stop() {
	
}
