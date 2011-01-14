package eu.aclement.oauthdemo

import eu.aclement.oauthdemo.services.DebugProcessor;

require group:'org.sdm', module:'http', revision:SDM_VERSION
require group:'org.sdm', module:'cxf', revision:SDM_VERSION
require group:'org.sdm', module:'camel', revision:SDM_VERSION

web(path:'/oauthdemo', war:'eu/aclement/oauthdemo/webapp/resources/') {
	
	initParams contextConfigLocation:'classpath:eu/aclement/oauthdemo/webapp/application-context.xml'
		
	listener className:'org.springframework.web.context.ContextLoaderListener'
	
	filter name:'springSecurityFilterChain', className:'org.springframework.web.filter.DelegatingFilterProxy'
	
	servlet(name:'CamelServlet', className:'org.apache.camel.component.servlet.CamelHttpTransportServlet', url:'/services/*') {
		bus = serviceRegistry.lookup('cxf')
	}
}

//core services
def bb = new_('grails.spring.BeanBuilder')
bb.beans {
	xmlns context:"http://www.springframework.org/schema/context", camelCxf:"http://camel.apache.org/schema/cxf", 
		util:"http://www.springframework.org/schema/util"
	
	context { 'annotation-config'() }
		
	userService(eu.aclement.oauthdemo.services.UserService.class)
	helloService(eu.aclement.oauthdemo.services.HelloService.class)
	jsonProvider(org.apache.cxf.jaxrs.provider.JSONProvider.class)
	totoExceptionMapper(eu.aclement.oauthdemo.services.TotoExceptionMapper.class)
	
	cxf(org.sdm.core.SDM.class, 'cxf') { bean -> bean.factoryMethod = 'getService' }	
	
	binding(eu.aclement.oauthdemo.cxfbean.HttpCxfBeanBinding.class)
	
	util { 
		list(id:'serviceBeans') {
			ref bean:'userService'
			ref bean:'helloService'
		}		
	}
}

def appCtx = bb.createApplicationContext()  

camelContext.disableJMX()

routes(appCtx) {
	
	//errorHandler defaultErrorHandler()
		
	from('servlet:///?matchOnUriPrefix=true') {
		process new DebugProcessor()
		
		to 'cxfbean:serviceBeans?bus=#cxf&cxfBeanBinding=#binding'	
	}
}

println 'oauthdemo started..'

def stop() {
	webapp.stop()
}
 