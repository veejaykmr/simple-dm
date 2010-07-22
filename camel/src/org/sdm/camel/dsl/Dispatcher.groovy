package org.sdm.camel.dsl

import groovy.lang.GroovyInterceptable;

class Dispatcher implements GroovyInterceptable {	
	
	def routeBuilder = new DefaultRouteBuilder()
	
	def invokeMethod(String m, args) {
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

