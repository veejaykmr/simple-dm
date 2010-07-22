package org.sdm.camel.dsl

class CamelMixin {	
	
	def camelContext = new_('org.apache.camel.spring.SpringCamelContext')
	
	def routes(closure) {
		def dispatcher = new Dispatcher() 
		closure.delegate = dispatcher
		closure()
		camelContext.addRoutes dispatcher.routeBuilder
	}	
}
