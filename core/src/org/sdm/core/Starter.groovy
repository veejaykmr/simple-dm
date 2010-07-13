package org.sdm.core;

import org.sdm.core.Module

import org.sdm.core.ServiceLocator;

class Starter {
	
	static start(String module) {
		def ex		
		try {
			def appMcl = Module.startModule(module)
		} catch(Throwable e) {
			println e
			ex = e
		}		
	}
	
	static stop(String module) {
		Module.stopModule(module)
	}
	
	static restart(String module) {
		stop module
		start module
	}
	
	static void main(args) {
		ServiceLocator.init()
		
		println "Simple Dynamic Module System version ${Module.SDM_VERSION} started."		
		
		def is = System.in
		def m		
		def test = { s, p -> m = s =~ p; m.matches() }
		def cur = 'org.sdm:testapp:' + Module.SDM_VERSION
		
		is.eachLine { line ->
			if (test(line, /^(start|stop|restart)\s+(.*)/)) {
				"${m[0][1]}"(m[0][2])
			} else if(test(line, /^stop|^start|^restart/)) {
				if (cur) {
					"${m[0]}"(cur)
				} else {
					println "Cannot exec a comand without a module key"
				}
			} else if(test(line, /^list/)) { 
				Module.list()
			} else {
				println "Unknown command: Usage: [start|stop] group:module:revision."
			}
		}
	}	
	
}