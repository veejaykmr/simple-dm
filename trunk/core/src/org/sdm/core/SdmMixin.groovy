package org.sdm.core


/**
 * This mixin provides the main SDM functions 
 *  - require
 *  - override
 *  etc...
 * @author alex
 *
 */
class SdmMixin {	
	
	static final SDM_VERSION = SDM.VERSION
	
	static depFmt = new DependencyFormat()
	
	// def moduleManager
	
	// def mcl
	
	def override(String dep) {
		override dep, (Map) null
	}
	
	def override(String key, String dep) {
		override key, depFmt.parse(dep)
	}
	
	def override(String key, Map dep) {
		override depFmt.parse(key), dep
	}
	
	def override(Map dep, Map over) {
		moduleManager.overrideDependency mcl, dep, over 
	}
	
	def require(String dep) { require depFmt.parse(dep) }
	
	def require(Map dep) {
		moduleManager.addDependency mcl, dep, this
	}		
	
	def new_(className) {
		Thread.currentThread().contextClassLoader.loadClass(className).newInstance()
	}
	
}
