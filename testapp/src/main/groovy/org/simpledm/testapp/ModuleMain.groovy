package org.simpledm.testapp;

require "org.simpledm:sdm-http:$SDM_VERSION"
require "org.simpledm:sdm-cxf:$SDM_VERSION"
require "org.simpledm:sdm-camel:$SDM_VERSION"

web(path:'/sdmtestapp', war:'org/simpledm/testapp/webapp/') {
		
	servlet(name:'CamelServlet', className:'org.apache.camel.component.servlet.CamelHttpTransportServlet', url:'/services/*') {
		bus = serviceRegistry.lookup('cxf')
	}
}

def bb = new_('grails.spring.BeanBuilder')
bb.beans {
	xmlns context:"http://www.springframework.org/schema/context", camelCxf:"http://camel.apache.org/schema/cxf",
		util:"http://www.springframework.org/schema/util"
	
	context { 'annotation-config'() }
			
	helloService(org.simpledm.testapp.HelloService.class)	
	
	// expose cxf bus service in this application context
	cxf(org.simpledm.core.SDM.class, 'cxf') { bean -> bean.factoryMethod = 'getService' }
	
	binding(org.simpledm.testapp.cxf.HttpCxfBeanBinding.class)
	
	util {
		list(id:'serviceBeans') {
			ref bean:'helloService'
		}
	}
}

def appCtx = bb.createApplicationContext()

routes(appCtx) {
	
	//errorHandler defaultErrorHandler()
		
	from('servlet:///?matchOnUriPrefix=true') {
		process new DebugProcessor()
		
		to 'cxfbean:serviceBeans?bus=#cxf&cxfBeanBinding=#binding'
	}
}

println 'sdm testapp started.'

def stop() {
	
}
