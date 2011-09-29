package org.simpledm.core.dsl;

import org.simpledm.core.DependencyFormat;

class Configuration {	
	
	def modules = []
	               
	def depFmt = new DependencyFormat()
	
	def directives = [:]
	
	boolean disableJMX
		
	def module(String key, clos) {
		def module = new Module(config: this)
		
		key = key.replace('*', '.*')
		def id = depFmt.parse(key)
				
		id.module = '^' + id.module + '$'
		id.revision = '^' + id.revision + '$'
		
		modules << module				
		module.id = id
		
		clos.resolveStrategy = Closure.DELEGATE_FIRST
		clos.delegate = module
		clos()
		
		module		
	}
	
	Module getModule(Map id) {
		modules.find { id.group == it.id.group && id.module =~ it.id.module && id.revision =~ it.id.revision }
	}
	
	def invokeMethod(String m, args) {
		assert args 
		directives[m] = args[0]
	}
	
}
