package org.simpledm.core

import java.util.Map;

import org.simpledm.core.config.Configuration;
import org.simpledm.core.config.Module;
import org.simpledm.core.utils.Log;

/**
 * @author alex
 * 
 * Dependency resolver that uses the ResolveEngine but adds a few features:
 * - overriding dependencies
 * - development staging awareness
 *
 */
class DependencyResolver {
	
	def resolveEngine
	
	def parentClassLoader
	                 
	final resolvingOptions = [transitive: false, autoDownload: true]	
	                          
	def depFormat = new DependencyFormat()
	
	def modules = []
	
	ModuleDescriptor resolveDependency(Map dep) {
		long now = System.currentTimeMillis()
		Log.trace("resolveDependency start for $dep...")
		
		def descriptor		
		def loader = Thread.currentThread().contextClassLoader
		Thread.currentThread().contextClassLoader = parentClassLoader		
		
		try {			
			def report = resolveEngine.resolve(resolvingOptions, dep)
			def resolvedDep = report.moduleDep
			
			def key = depFormat.toString(resolvedDep)
			// first uri should be the dep URI itself, followers are uris of
			// transitive dependencies
			def moduleUrl = report.moduleUri.toURL()
			
			descriptor = new ModuleDescriptor()
			descriptor.moduleUrls = [moduleUrl].asImmutable()
			descriptor.moduleDeps = report.moduleDeps
			descriptor.uris = report.uris
			
			//see if the module is in development stage
			Module moduleConf = getModule(resolvedDep)
	 		if (moduleConf) { 
	 			descriptor.moduleDeps = applyOverrides(report.moduleDeps, moduleConf.overrides)
				descriptor.developmentStage = moduleConf.dirs.size() > 0
				if (descriptor.developmentStage) {
					def uris = moduleConf.dirs.collect { new File(it).toURI() }.asImmutable()
					//TODO checks uris existence
					descriptor.moduleUrls = uris.collect { it.toURL() }.asImmutable()
					descriptor.uris = (uris + (report.uris as List).tail()).asImmutable()	
				}							
			} 
		} finally {
			Thread.currentThread().contextClassLoader = loader
		}
		
		long dur = System.currentTimeMillis() - now
		Log.trace("resolveDependency to ${descriptor.moduleDep} took $dur ms.")
		
		descriptor
	}	
		
	def applyOverrides(deps, overrideDeps) {
		if (!overrideDeps) 
			return deps
			
		def results = deps.collect { dep ->
            def result = dep
            def matchingOverride = overrideDeps.find { o -> o[0].group == dep.group && dep.module =~ o[0].module }
            if (matchingOverride) {
                def mo = matchingOverride              
                result = [group: (mo.last()?:dep).group, module: (mo.last()?:dep).module, revision: (mo.last()?:mo.first()).revision]
                result = resolveEngine.resolve(resolvingOptions, result).moduleDep
            }            
            result
        }      
		results.asImmutable()
    }	
	
	void addModule(Module module){
		modules << module
	}
	
	Module getModule(Map id) {
		modules.find { id.group == it.id.group && id.module =~ it.id.module && id.revision =~ it.id.revision }
	}
}
