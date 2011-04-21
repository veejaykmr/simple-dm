package org.sdm.core

class DependencyFormat {
	
	Map parse(String dep) {
		def result
		
		def m
		def match = { s, p -> m = s =~ p; m.matches() }		
		
		if (match(dep, /(.*):(.*):(.*)/)) {
			result = [group: m[0][1], module: m[0][2], revision: m[0][3]]
		} else if (match(dep, /(.*):(.*)/)) {		
			result = [group: m[0][1], module: m[0][2]]
		} else {
			// we suppose we have only the group id
			// we infer the artifact id using SDM naming conventions
			def module = dep.replace('.', '-')
			result = [group: dep, module: module]
		}
		result	
	}
	
	String toString(Map dep) {
		assert dep.group && dep.module		
		def rev = dep.revision ?: dep.version ?: dep.rev
		rev ? "${dep.group}:${dep.module}:${rev}" : "${dep.group}:${dep.module}"
	}

}
