package org.simpledm.core;

import org.simpledm.core.utils.Log;

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
			
			def moduleDeps = []
			def uris = grape.resolve(cl, args, moduleDeps, newDep) as List
			assert uris
						
			//unique (ignoring version)
			moduleDeps.unique { a,b -> a.module == b.module && a.group == b.group ? 0 : 1 }
			
			result.moduleDeps = new LinkedHashSet(moduleDeps).asImmutable()
			result.uris = new LinkedHashSet(uris).asImmutable()
			
			// grape bug? if dep version is a range, it is not resolved by default in the returned moduledeps
			// but uris are correct, set the resolved version number.
			def moduleDep = result.moduleDep
			def module = moduleDep.module
			def m = result.moduleUri =~ /${module}-(.*)\.jar/
			def version = m[0][1]
			moduleDep.revision = version
			
			cache[key] = result
		}
		
		long dur = System.currentTimeMillis() - now
		Log.trace("Resolving module $dep to ${result.moduleDep} took $dur ms.")
		
		result
	}
	
}
