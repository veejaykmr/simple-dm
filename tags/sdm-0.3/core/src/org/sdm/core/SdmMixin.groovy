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
	
	// def moduleManager
	
	// def mcl
	
	def override(key, dep) {
		moduleManager.addAlias(key, dep)
	}
	
	def require(dep) {
		mcl.addDependency dep
		moduleManager.assureModuleStarted dep								
		moduleManager.notifyModuleRequired requiringDep: mcl.moduleDep, requiredDep: dep, requiringObject: this				
		// Set the context classloader after loading the runtime dependency
		Thread.currentThread().setContextClassLoader mcl
	}		
	
	def new_(className) {
		Thread.currentThread().contextClassLoader.loadClass(className).newInstance()
	}
}
