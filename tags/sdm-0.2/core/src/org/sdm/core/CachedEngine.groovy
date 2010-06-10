package org.sdm.core;

import java.util.Map;

import groovy.grape.Grape;

class CachedEngine {
		
	def resolver = ServiceLocator.getResolver()
	
	def engine = Grape.getInstance();
	
	def cache = [:]
	
	ResolveReport resolve(ClassLoader classLoader, Map args, Map dep) {
		def key = resolver.getModuleKey(dep) as String		
		
		ResolveReport result = cache[key]
		if (!result) {
			result = new ResolveReport()
			result.uris = engine.resolve(classLoader, args, result.moduleDeps, dep)
			cache[key] = result
		}
		
		result
	}
	
}
