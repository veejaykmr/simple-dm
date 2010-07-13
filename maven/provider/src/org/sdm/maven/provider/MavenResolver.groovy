package org.sdm.maven.provider

import java.util.Map;

class MavenResolver {
	
	def mappings = [:]
	
	MavenResolver() {
		def loader = new GroovyClassLoader()
		int i = 0
		while (true) {
			def is = loader.getResourceAsStream("org/sdm/maven/metadata/ModuleMetadata${i++}.groovy")
			if (!is)
				break			
			def metadata = loader.parseClass(is).newInstance()
			mappings.putAll metadata.resolveMap
		}		
	}
	
	/**
	 * TODO delegate to a maven specific resolver
	 * @param className
	 * @param moduleDeps
	 * @return
	 */
	List resolveModule(className, moduleDeps) {
		//could not resolve module name based on package naming convention, try mapping metadata
		def pkgName = getPackageName(className)			
		def modules = mappings[pkgName] ?: []
				
		def results = moduleDeps.findAll { m -> modules.find { it.group ==  m.group && it.module == m.module } }
		if (results) {
			return results
		}
		
		def mod = guessModuleFromClassName(className, moduleDeps)
		mod ? [mod] : []
	}
	
	private guessModuleFromClassName(className, moduleDeps) {
		def result = null
		// try to deduce (maven) module name from class package name
		// exemple: org.apache.camel.spring ==> org.apache.camel:camel-spring
		def pkgName = getPackageName(className)
		def pkgWords = pkgName.split(/\./)
		
		def sameGroupModules = moduleDeps.findAll { dep ->
			pkgName.startsWith dep.group
		}			
		
		if(sameGroupModules) {
			def group = sameGroupModules.first().group
			def groupAsWords = group.split(/\./) as List
			def lastWords = pkgWords - groupAsWords
			
			//try to determine module id
			result = sameGroupModules.find { dep ->
				lastWords.find { w -> dep.module.contains(w) }
			}
		}
		result
	}
	
	/**
	 * TODO delegate to a maven specific resolver
	 * @param dep
	 * @return
	 */
	def resolveModuleMainClassName(dep) {
		def words = dep.module.split('-') as List				
		"${dep.group}.${words.last()}.ModuleMain"
	}
	
	/**
	 * Build a unique key for a module dependency 
	 * 
	 * TODO delegate to a maven specific resolver
	 * @param dep
	 * @return
	 */
	def getModuleKey(Map dep) {
		assert dep.group && dep.module && dep.revision
		"${dep.group}:${dep.module}:${dep.revision}"
	}
	
	Map keyToMap(String dep) {
		def m = dep =~ /(.*):(.*):(.*)/
		assert m.matches()
		
		[group: m[0][1], module: m[0][2], revision: m[0][3]]	
	}
	
	private getPackageName(className) {
		def classNameAsWords = className.split(/\./) as List
		classNameAsWords.pop()
		def pkgName = classNameAsWords.join('.')
	}
}
