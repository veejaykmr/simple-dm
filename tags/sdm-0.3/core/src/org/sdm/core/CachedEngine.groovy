package org.sdm.core;

import groovy.grape.Grape;

class CachedEngine {
		
	def resolver
	
	def engine = Grape.getInstance();
	
	def cache = [:]
	
	ResolveReport resolve(ClassLoader classLoader, Map args, Map dep) {
		// we need to keep only these attributes to make resolving work
		def newDep = [group: dep.group, module: dep.module, revision: dep.revision]
		def key = resolver.getModuleKey(newDep) as String		
		
		ResolveReport result = cache[key]
		if (!result) {
			result = new ResolveReport()
			result.uris = engine.resolve(classLoader, args, result.moduleDeps, newDep)
			cache[key] = result
		}
		
		result
	}
	
}
