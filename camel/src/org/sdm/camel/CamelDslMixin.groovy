package org.sdm.camel

import groovy.lang.Closure;

import static org.sdm.core.utils.Classes.*

class CamelDslMixin {	
	
	def camelContext = new_('org.apache.camel.spring.SpringCamelContext')
	
	def routeBuilder
	
	def routes(closure) {
		routeBuilder = new DefaultRouteBuilder()
		closure()
		camelContext.addRoutes routeBuilder
	}

	def methodMissing(String m, args) {
		args = args as List
		if (args && args.last() instanceof Closure) {
			def last = args.pop()
			def definition = routeBuilder."$m"(*args)
			last.delegate = definition
			last()
		} else {
			routeBuilder."$m"(*args)
		}
	}
}
