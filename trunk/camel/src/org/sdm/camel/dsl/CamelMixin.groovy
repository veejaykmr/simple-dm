package org.sdm.camel.dsl

class CamelMixin {	
	
	def camelContext = new_('org.apache.camel.spring.SpringCamelContext')
	
	def routes(appCtx, closure) {
		camelContext.applicationContext = appCtx 				
		routes closure
	}
	
	def routes(closure) {
		def dispatcher = new Dispatcher(target: new DefaultRouteBuilder())
		closure.resolveStrategy = Closure.DELEGATE_FIRST
		closure.delegate = dispatcher
		closure()
		camelContext.addRoutes dispatcher.target		
		camelContext.start()
		camelContext
	}	
}
