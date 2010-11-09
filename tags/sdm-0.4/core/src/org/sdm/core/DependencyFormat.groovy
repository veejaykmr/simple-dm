package org.sdm.core

class DependencyFormat {
	
	Map parse(String dep) {
		def result
		
		def m = dep =~ /(.*):(.*):(.*)/
		if(m.matches()) {
			result = [group: m[0][1], module: m[0][2], revision: m[0][3]]
		} else {
			m = dep =~ /(.*):(.*)/
			assert m.matches()
			result = [group: m[0][1], module: m[0][2]]
		}
		result	
	}
	
	String toString(Map dep) {
		assert dep.group && dep.module		
		def rev = dep.revision ?: dep.version ?: dep.rev
		rev ? "${dep.group}:${dep.module}:${rev}" : "${dep.group}:${dep.module}"
	}

}
