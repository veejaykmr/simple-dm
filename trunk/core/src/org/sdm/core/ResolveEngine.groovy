package org.sdm.core;

import org.sdm.core.utils.Log;

import groovy.grape.Grape;

/**
 * 
 * Dependency Resolver engine that uses Grape to resolve dependencies and put them in a cache.
 * 
 * @author alex
 *
 */
class ResolveEngine {
		
	def grape = Grape.getInstance();
	
	def cache = [:]
	             
	def cl = new GroovyClassLoader()
	
	def depFmt = new DependencyFormat()
	
	ResolveReport resolve(Map args, Map dep) {
		long now = System.currentTimeMillis()
		// we need to keep only these attributes to make resolving work
		def newDep = [group: dep.group, module: dep.module, revision: dep.revision]
		def key = depFmt.toString(newDep)
		
		ResolveReport result = cache[key]
		if (!result) {
			result = new ResolveReport()			
			
			grape.loadedDeps.clear() 
			result.uris = grape.resolve(cl, args, result.moduleDeps, newDep)
			assert result.uris
			
			// grape bug? if dep version is a range, it is not resolved by default in the returned moduledeps
			// but uris are correct, set the resolved version number.
			def module = result.moduleDeps[0].module
			def m = result.uris[0] =~ /${module}-(.*)\.jar/
			def version = m[0][1]
			result.moduleDeps[0].revision = version
			
			cache[key] = result
		}
		
		long dur = System.currentTimeMillis() - now
		Log.trace("Resolving module $dep to ${result.moduleDep} took $dur ms.")
		
		result
	}
	
}
