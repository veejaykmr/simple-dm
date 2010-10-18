package org.sdm.core.dsl;

import org.sdm.core.DependencyFormat;

class Module {
	
	def config
	
	Map id
	
	List<String> dirs = []
	                        
	def overrides = [] 
	                 
	def depFmt = new DependencyFormat()
	                        
	def dir(String path) {
		assert path
		dirs << path
	}
	
	def override(String dep, String over) {
		dep = dep.replace('*', '.*')	
		overrides << [depFmt.parse(dep), depFmt.parse(over)]
	}
	
	def override(String dep) {
		dep = dep.replace('*', '.*')	
		overrides << [depFmt.parse(dep), null]
	}
	
	def invokeMethod(String m, args) {
		def clos = config.directives[m]
		assert clos instanceof Closure
		clos.delegate = this
		clos.resolveStrategy = Closure.DELEGATE_FIRST
		clos()
	}
}

