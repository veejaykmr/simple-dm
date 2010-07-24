package org.sdm.core;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.sdm.core.utils.Log;

import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;

import java.io.OutputStream;

class ModuleManager {	
	
	def parentClassLoader 
	
	def resolver
	
	def engine
	
	def serviceRegistry
	
	def configuration	
	
	def serviceLocator
	
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
	 * Module dependency aliases
	 */
	def aliases = [:]
	
	/**
	 * Get the MCL for the given module dependency
	 * 
	 * @param moduleDep
	 * @return
	 */
	ModuleClassLoader getMcl(Map moduleDep) {
		def key = resolver.getModuleKey(moduleDep)
		def mcl = mclMap[key]
		if(!mcl) {
			mcl = serviceLocator.moduleClassLoader(parentClassLoader, moduleDep)
			mclMap[key] = mcl
		}
		mcl
	}
	
	String getKey(Map dep) {
		resolver.getModuleKey(dep) as String
	}
	
	def addAlias(key, value) {
		aliases[key] = value
	}
	
	def getModuleMainInstance(Map dep) {
		mainInstanceMap[getKey(dep)]
	}
	
	List resolveModule(className, moduleDeps) {
		resolver.resolveModule className, moduleDeps
	}
	
	def startModule(String dep) {
		startModule resolver.keyToMap(dep)
	}
	
	def startModule(Map dep) {	
		long now = System.currentTimeMillis()
		Log.info("Starting module $dep ...")
		
		def key = getKey(dep)
		def mcl = getMcl(dep)
		assert mcl
		
		def mainClassName = resolver.resolveModuleMainClassName(dep)
		try {
			Thread.currentThread().setContextClassLoader mcl
			
			Class mainClass = mcl.loadClass(mainClassName)	
			mainClass.mixin SdmMixin
			
			def object = mainClass.newInstance()
			mainInstanceMap[key] = object
			
			object.metaClass {
				moduleManager = this
				delegate.mcl = mcl
				delegate.serviceRegistry = this.serviceRegistry
			}
			
			notifyModuleStarting dep: dep, key: key, object: object
			
			object.invokeMethod 'run', null				
			
		} catch(ClassNotFoundException e) {
			Log.trace("Module " + dep + " doesn't have a main class: " + mainClassName);
		} 			
		
		mcl.start()
		
		long dur = System.currentTimeMillis() - now
		Log.info("Module $dep started in $dur ms.")
		
		mcl
	}
	
	def stopModule(String dep) {
		stopModule resolver.keyToMap(dep)
	}
	
	def stopModule(Map dep) {
		long now = System.currentTimeMillis()
		Log.info("Stopping module $dep ...")
		
		if (!isModuleStarted(dep)) {
			Log.trace("Cannot stop a module that is not started!")
			return
		}
		
		def key = getKey(dep) as String
		def mcl = getMcl(dep)
		assert mcl
		
		mcl.stop()
		
		try {
			def object = mainInstanceMap[key]
			if (object) {
				object.stop()
			}
		} catch(MissingMethodException e) {
			Log.trace("Module " + dep + " doesn't have a stop method: " + mainClassName);
		} 		
		
		mcl.finalize()
		mclMap.remove key
		mainInstanceMap.remove key	
		
		long dur = System.currentTimeMillis() - now
		Log.info("Module $dep stopped in $dur ms.")
	}
	
	def restartModule(String dep) {
		restartModule resolver.keyToMap(dep)
	}
	
	def restartModule(Map dep) {
		if (isModuleStarted(dep)) {
			stopModule dep
		}
		startModule dep
	}
	
	boolean isModuleStarted(Map dep) {
		def key = getKey(dep)
		mclMap.containsKey key
	}
	
	def assureModuleStarted(String dep) {
		assureModuleStarted resolver.keyToMap(dep)
	}
	
	def assureModuleStarted(Map dep) {
		if (!isModuleStarted(dep)) {
			startModule dep
		}
	}
	
	/**
	 * Notify modules that a module is starting
	 * @param args
	 * @return
	 */
	def notifyModuleStarting(Map args) {
		mainInstanceMap.each { k,v ->
			try {	
				 k != args.key && v.onModuleStarting(args)
			} catch(MissingMethodException e) {}
		}
	}
	
	/**
	 * Notify the required module
	 * @param args
	 * @return
	 */
	def notifyModuleRequired(Map args) {
		def instance = getModuleMainInstance(args.requiredDep)
		try {	
			instance?.onRequire args
		} catch(MissingMethodException e) {
		
		}		
	}
	
	ResolveReport resolveDependencies(classLoader, Map dep) {
		def args = [classLoader: classLoader, transitive: true, autoDownload: true]		
		def report = engine.resolve(classLoader, args, dep)
		report.moduleDeps = substituteAliases(report.moduleDeps);
		report
	}
	
	def substituteAliases(deps) {
		deps.collect { aliases[getKey(it)] ?: it }.flatten()			
	}
	
	def setResourceContext(mcl) {
		rccl = mcl
	}
	
	def getResourceContext() {
		rccl
	}
	
	def list() {
		mclMap.each { key,mcl -> 
			println "$key (${mcl.loadedClasses.size()} classes)"
		}
	}
	
	def listModules() {
		def results = []
		mclMap.each { key,mcl -> 
			results << "$key (${mcl.loadedClasses.size()} classes)"
		}             
		results
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
			classes.each { c ->  println "\t$c" }
			println ''
		}
	}
	
}


