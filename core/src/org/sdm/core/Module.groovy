package org.sdm.core;

import java.lang.reflect.Method;

import org.sdm.core.utils.Log;

import groovy.lang.MissingPropertyException;

public class Module {
	
	private static ModuleManager instance = new ModuleManager()

	static List resolveModule(className, moduleDeps) {
		instance.resolveModule(className, moduleDeps)
	}

	static startModule(dep) {
		instance.startModule dep 
	}
	
	static stopModule(dep) {
		instance.stopModule dep 
	}
	
	static ModuleClassLoader getMcl(dep) {
		instance.getMcl dep
	}
	
	static setResourceContext(ModuleClassLoader mcl) {
		instance.setResourceContext mcl
	}
	
	static ModuleClassLoader getResourceContext() {
		instance.getResourceContext()
	}
	
	static list() {
		instance.list()
	}	
	
	static dump() {
		instance.dump()
	}	
			
	static class ModuleManager {		
		
		// strategy pattern
		def resolver = Class.forName('org.sdm.maven.provider.MavenResolver').newInstance()
		
		/**
		 * map module name to module classloader (MCL)
		 */
		Map mclMap = [:]	
		
		/**
		 * Resource context classloader; use to load application resources 
		 */
		def rccl
		
		/**
		 * main instances
		 */
		Map mainInstanceMap = [:]
		
		/**
		 * Get the MCL for the given module dependency
		 * 
		 * @param moduleDep
		 * @return
		 */
		def getMcl(Map moduleDep) {
			def key = resolver.getModuleKey(moduleDep)
			def mcl = mclMap[key]
			if(!mcl) {
				def parent = this.getClass().getClassLoader()
				mcl = new ModuleClassLoader(parent, moduleDep)
				mclMap[key] = mcl
			}
			mcl
		}
				
		def getKey(Map dep) {
			resolver.getModuleKey(dep) as String
		}
		
		List resolveModule(className, moduleDeps) {
			resolver.resolveModule className, moduleDeps
		}
				
		def startModule(dep) {	
			long now = System.currentTimeMillis()
			Log.info("Starting module $dep ...")
						
			def key = getKey(dep)
			def mcl = getMcl(dep)
			assert mcl
			
			def mainClassName = resolver.resolveModuleMainClassName(dep)
			try {
				Thread.currentThread().setContextClassLoader mcl
				
				Class mainClass = mcl.loadClass(mainClassName)						
				Object object = mainClass.newInstance();
				
				// Start runtime dependencies
				try {
					def runtimeDeps = object.runtimeDependencies
					runtimeDeps.each { assureModuleStarted it }
				} catch(MissingPropertyException e) {
					// nothing to do
				} 			
				// Set the context classloader after loading runtime dependencies
				Thread.currentThread().setContextClassLoader mcl
				
				object.start()
				mainInstanceMap[key] = object
			} catch(ClassNotFoundException e) {
				Log.trace("Module " + dep + " doesn't have a main class: " + mainClassName);
			} 
		
			long dur = System.currentTimeMillis() - now
			Log.info("Module $dep started in $dur ms.")
		}
		
		def stopModule(dep) {
			long now = System.currentTimeMillis()
			Log.info("Stopping module $dep ...")
			
			if (!isModuleStarted(dep)) {
				Log.trace("Cannot stop a module that is not started!")
				return
			}
			
			def key = getKey(dep) as String
			def mcl = getMcl(dep)
			assert mcl
			
			try {
				def object = mainInstanceMap[key]
				if (object) {
					object.stop()
				}
			} catch(ClassNotFoundException e) {
				Log.trace("Module " + dep + " doesn't have a main class: " + mainClassName);
			} 		
		
			mcl.finalize()
			mclMap.remove key
			mainInstanceMap.remove key	
			
			long dur = System.currentTimeMillis() - now
			Log.info("Module $dep stopped in $dur ms.")
		}
		
		def restartModule(dep) {
			if (isModuleStarted(dep)) {
				stopModule dep
			}
			startModule dep
		}
				
		boolean isModuleStarted(Map dep) {
			def key = getKey(dep)
			mclMap.containsKey key
		}
		
		def assureModuleStarted(Map dep) {
			if (!isModuleStarted(dep)) {
				startModule dep
			}
		}
		
		def setResourceContext(mcl) {
			rccl = mcl
		}
		
		def getResourceContext() {
			rccl
		}
		
		def list() {
			mclMap.each { key,mcl -> 
				println key
			}
		}
		
		def dump() {
			//def parentPkgs = getClass().getClassLoader().packages
			
			mclMap.each { key,mcl -> 
				def urls = mcl.getURLs()
				def classes = mcl.loadedClasses
			//	def pkgs = mcl.packages - parentPkgs
				println ''
				println "MCL key: $key"
				println " Url: $urls"
				println " Class list:"
				classes.each { c -> 
					println "\t$c"
				}
				println ''
			}
		}
	
	}

}
