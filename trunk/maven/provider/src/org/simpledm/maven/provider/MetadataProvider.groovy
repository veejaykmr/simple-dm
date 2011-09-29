package org.simpledm.maven.provider

import java.util.Map;

/**
 * Maven module metadata provider
 */
class MetadataProvider {
	
	def mappings = [:]
	
	MetadataProvider() {
		def loader = new GroovyClassLoader()
		int i = 0
		while (true) {
			def is = loader.getResourceAsStream("org/simpledm/maven/metadata/ModuleMetadata${i++}.groovy")
			if (!is)
				break			
			def metadata = loader.parseClass(is).newInstance()
			mappings.putAll metadata.resolveMap
		}		
	}
	
	/**
	 * Resolve the module the class belongs to
	 * 
	 * @param className
	 * @param moduleDeps
	 * @return
	 */
	def resolveModule(className, moduleDeps) {
		//could not resolve module name based on package naming convention, try mapping metadata
		def pkgName = getPackageName(className)			
		def modules = mappings[pkgName] ?: []
				
	    def results = moduleDeps.findAll { m -> modules.find { it.group == m.group && it.module == m.module } }
		
		results
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
		
	private getPackageName(className) {
		def classNameAsWords = className.split(/\./) as List
		classNameAsWords.pop()
		def pkgName = classNameAsWords.join('.')
	}
}
