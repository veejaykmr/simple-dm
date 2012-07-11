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
		starter.startRootModule()			
				
		println "Simple Dynamic Module System version ${SDM.VERSION} started."		
					
		System.in.eachLine { line ->
			(line =~ /^(start|stop|restart)\s+(.*)/).each { all, cmd, cur ->
				starter."$cmd"(cur)
				return
			}			
			(line =~ /^list$/).each {  
				starter.list()
				return
			}
			(line =~ /^listClasses/).each { 
				starter.listClasses()
				return
			}
			(line =~ /^deps/).each { 
				starter.deps()
				return
			}
			println "Unknown command: Usage: [start|stop] group:module:revision."			
		}
	}	
	
}