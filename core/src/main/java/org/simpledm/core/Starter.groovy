package org.simpledm.core;

import org.simpledm.core.ServiceLocator;

class Starter {
	
	def moduleManager = ServiceLocator.instance().moduleManager
	
	def start(String module) {
		module = module.trim()
		moduleManager.startModule(module)
	}
	
	def stop(String module) {
		module = module.trim()
		moduleManager.stopModule(module)
	}
	
	def restart(String module) {
		module = module.trim()
		stop module
		start module
	}
	
	def list() {
		moduleManager.list()
	}
	
	def listClasses() {
		moduleManager.listClasses()
	}
	
	def deps() {
		moduleManager.deps()
	}
	
	def startRootModule(){		
		def rootDep = ServiceLocator.instance().rootModuleDep	
		moduleManager.startModule rootDep
	}
	
	static void main(String[] args) {
		
		ServiceLocator.initialize()
		
		def starter = new Starter()
		
		// root module
        String module = System.getProperty('sdm.root.module') ?: "org.simpledm:sdm-root:${SDM.VERSION}"
		starter.start module			
				
		println "Simple Dynamic Module System version ${SDM.VERSION} started."		
					
		System.in.eachLine { line ->
			boolean ok = false
			
			line.eachMatch(/^(start|stop|restart)\s+(.*)/) { all, cmd, mod ->
				starter."$cmd"(mod)	
				ok = true			
			}			
			(line =~ /^list$/).each {  
				starter.list()
				ok = true
			}
			(line =~ /^listClasses/).each { 
				starter.listClasses()
				ok = true
			}
			(line =~ /^deps/).each { 
				starter.deps()
				ok = true
			}
			
			if (!ok)
				println "Unknown command: Usage: [start|stop] group:module:revision."					
		}
	}	
	
}