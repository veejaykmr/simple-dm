package org.sdm.core;

import org.sdm.core.ServiceLocator;

class Starter {
	
	def moduleManager = ServiceLocator.instance().moduleManager
	
	def start(String module) {
		def ex		
		try {
			def appMcl = moduleManager.startModule(module)
		} catch(Throwable e) {
			println e
			ex = e
		}		
	}
	
	def stop(String module) {
		moduleManager.stopModule(module)
	}
	
	def restart(String module) {
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
	
	static void main(args) {
		ServiceLocator.initialize()
		def starter = new Starter()		
		
		println "Simple Dynamic Module System version ${SDM.VERSION} started."		
		
		def is = System.in
		def m		
		def test = { s, p -> m = s =~ p; m.matches() }
		def cur = 'org.sdm:testapp:' + SDM.VERSION
		
		is.eachLine { line ->
			if (test(line, /^(start|stop|restart)\s+(.*)/)) {
				cur = m[0][2]
				starter."${m[0][1]}"(cur)
			} else if(test(line, /^stop|^start|^restart/)) {
				if (cur) {
					starter."${m[0]}"(cur)
				} else {
					println "Cannot exec a comand without a module key"
				}
			} else if(test(line, /^list$/)) { 
				starter.list()
			} else if(test(line, /^listClasses/)) { 
				starter.listClasses()
			}  else if(test(line, /^deps/)) { 
				starter.deps()
			}else {
				println "Unknown command: Usage: [start|stop] group:module:revision."
			}
		}
	}	
	
}