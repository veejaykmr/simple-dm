package org.sdm.core

import java.util.Map;

import org.sdm.core.dsl.Project;
import org.sdm.core.utils.Log;

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
	
	def configuration
	
	def parentClassLoader
	                 
	final resolvingOptions = [transitive: false, autoDownload: true]	
	                          
	def depFormat = new DependencyFormat()
	
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
			def moduleUrl = report.uris.first().toURL()
			
			descriptor = new ModuleDescriptor()
			
			def overrides = loader instanceof ModuleClassLoader ? loader.overrides : null
			descriptor.moduleDeps = applyOverrides(report.moduleDeps, overrides)
			
			//see if the module is in development stage
			Project project = configuration.getProject(key)
	 		if (project) { 
				descriptor.developmentStage = true;
				def uris = project.sources.collect { new File(it).toURI() }
				descriptor.moduleUrls = uris.collect { it.toURL() }
				descriptor.uris = uris + report.uris.tail()		
			} else {
				descriptor.moduleUrls = [moduleUrl]
				descriptor.uris = report.uris
			}
		} finally {
			Thread.currentThread().contextClassLoader = loader
		}
		
		long dur = System.currentTimeMillis() - now
		Log.trace("resolveDependency to ${descriptor.moduleDep} took $dur ms.")
		
		descriptor
	}	
		
	def overrideDependency(mcl, Map dep, Map overDep) {
		def loader = Thread.currentThread().contextClassLoader
		assert loader instanceof ModuleClassLoader
		
		dep.module.replace '*', '(.*)'
				
		def override = [dep, overDep]
		//keep overrides for later
		loader.overrides << override
		
		mcl.moduleDeps = applyOverrides(mcl.moduleDeps, [override])
	}
	
	def applyOverrides(deps, overrideDeps) {
		if (!overrideDeps) 
			return deps
			
		deps.collect { dep ->
            def result = dep
            def matchingOverride = overrideDeps.find { o -> o[0].group == dep.group && dep.module =~ o[0].module }
            if (matchingOverride) {
                def mo = matchingOverride              
                result = [group: (mo.last()?:dep).group, module: (mo.last()?:dep).module, revision: (mo.last()?:mo.first()).revision]
                //if (result.revision =~ /^[\[(]/) {
                	// range version
                    result = resolveEngine.resolve(resolvingOptions, result).moduleDep
                //}
            }            
            result
        }      
    }
}
