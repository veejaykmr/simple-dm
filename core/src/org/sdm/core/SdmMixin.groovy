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
		
	}
	
	def override(String key, String dep) {
		
	}
	
	def override(String key, Map dep) {
		
	}
	
	def override(Map dep, Map over) {
		
	}
	
	def require(String dep) { require depFmt.parse(dep) }
	
	def require(Map dep) {
		moduleManager.addDependency mcl, dep, this
	}		
	
	def new_(className) {
		def result = null
		try {
			result = Thread.currentThread().contextClassLoader.loadClass(className).newInstance()
		} catch(Throwable t) {
		System.err.println ">>>>Stack trace begin"
		   while (t) {
			   System.err.println t
			   t = t.cause
		   }	
		   System.err.println ">>>>Stack trace end"
		}
		result
	}
	
	def with(String key, clos) {
		def dep = depFmt.parse(key)
		def loader = Thread.currentThread().contextClassLoader
		def withMcl = moduleManager.getMcl(dep)
		assert withMcl
		
		try {
			Thread.currentThread().contextClassLoader = withMcl
			clos()
		} finally {
			Thread.currentThread().contextClassLoader = loader
		}		
	}
	
}
