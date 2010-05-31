package org.sdm.core;

import org.sdm.core.Module

class Starter {
	
	static start(module) {
		def ex		
		try {
			def appMcl = Module.startModule(module)
		} catch(Throwable e) {
			println e
			ex = e
		}		
	}
	
	static stop(module) {
		Module.stopModule(module)
	}
	
	static restart(module) {
		stop module
		start module
	}
	
	static void main(args) {
		println "Simple Dynamic Module System started."
		
		def is = System.in
		def m		
		def test = { s, p -> m = s =~ p; m.matches() }
		def cur = [group: 'org.sdm', module: 'testapp', revision: '1.0-SNAPSHOT']
		
		is.eachLine { line ->
			if (test(line, /^(start|stop|restart)\s+(.*):(.*):(.*)/)) {
				cur = [group: m[0][2], module: m[0][3], revision: m[0][4]]
				"${m[0][1]}"(cur)
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