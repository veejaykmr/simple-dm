package org.simpledm.core.config;

import org.simpledm.core.DependencyFormat;
import org.simpledm.core.DependencyResolver;
import org.simpledm.core.ServiceLocator;


class Configuration {	
	
	DependencyResolver resolver = ServiceLocator.instance().dependencyResolver	
	               
	def depFmt = new DependencyFormat()
	
	def directives = [:]
		
	def module(String key, clos) {
		def module = new Module(config: this)
		
		key = key.replace('*', '.*')
		def id = depFmt.parse(key)
				
		id.module = '^' + id.module + '$'
		id.revision = '^' + id.revision + '$'
		
		module.id = id
		
		resolver.addModule module		
		
		clos.resolveStrategy = Closure.DELEGATE_FIRST
		clos.delegate = module
		clos()
		
		module		
	}
	
	void enableJMX() {
		ServiceLocator.instance().enableJMX()
	}		
	
	def directive(String directive, clos) {
		assert clos 
		directives[directive] = clos
	}
	
}
