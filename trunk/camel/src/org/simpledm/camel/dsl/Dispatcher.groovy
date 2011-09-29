package org.simpledm.camel.dsl

import groovy.lang.GroovyInterceptable;

class Dispatcher implements GroovyInterceptable {	
	
	def target
	
	def invokeMethod(String m, args) {
		args = args as List
		if (args && args.last() instanceof Closure) {
			def clos = args.pop()
			clos.resolveStrategy = Closure.DELEGATE_FIRST
			def definition = target."$m"(*args)
			def dispatcher = new Dispatcher(target: definition)
			clos.delegate = dispatcher
			clos.call()
			//m == 'choice' && definition.end()
		} else {
			target."$m"(*args) 
		}
	}
}

