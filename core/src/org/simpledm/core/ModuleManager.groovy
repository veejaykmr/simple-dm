package org.simpledm.core;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.simpledm.core.dsl.Module;
import org.simpledm.core.utils.Log;
import org.simpledm.core.utils.Utils;

import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;

import java.io.File;
import java.io.OutputStream;

class ModuleManager {	
		
	def metadataProvider
	
	def serviceRegistry
		
	def serviceLocator
	
	def dependencyResolver
	
	/**
	 * map module name to module classloader (MCL)
	 */
	Map mclMap = [:]	
		
	/**
	 * main instances
	 */
	Map mainInstanceMap = [:]	
	                          
	def depFormat = new DependencyFormat()
	
	/**
	 * Get the MCL for the given module dependency
	 * 
	 * @param moduleDep
	 * @return
	 */
	ModuleClassLoader getMcl(Map dep) {
		mclMap[depFormat.toString(dep)]		
	}
		
	def getModuleMainInstance(Map dep) {
		mainInstanceMap[depFormat.toString(dep)]
	}
	
	/**
	 * Get the module that contains the given class within the given dependencies
	 * @param className
	 * @param moduleDeps
	 * @return
	 */
	Collection resolveModule(className, moduleDeps) {
		metadataProvider.resolveModule className, moduleDeps
	}
	
	def resolveModuleBasePackage(dep) {
		def words = dep.module.split('-') as List
        "${dep.group}.${words.last()}"
	}
	
	def resolveModuleMainClassName(dep) {
		resolveModuleBasePackage(dep) + ".ModuleMain"
	}
	
	def startModule(String dep) {
		startModule depFormat.parse(dep)
	}
	
	def startModule(Map dep) {		
		Log.info "Resolving..."
		def md = resolveDependency(dep)
		startModule md		
	}
	
	def startModule(ModuleDescriptor md) {		
		long now = System.currentTimeMillis()		
		
		def mcl
		def resolvedDep = md.moduleDep		
		def loader = Thread.currentThread().contextClassLoader
	
		if (isModuleStarted(md)) {
			Log.info("Module $resolvedDep is already started.")
			return
		}		
		Log.info("Starting module $resolvedDep ...")	
		
		try {
			def key = depFormat.toString(resolvedDep)
			mcl = serviceLocator.moduleClassLoader(md)
			mclMap[key] = mcl			
					
			Thread.currentThread().contextClassLoader = mcl			
			
			try {
				def mainClassName = resolveModuleMainClassName(resolvedDep)
				Class mainClass = mcl.loadClass(mainClassName)	
				mainClass.mixin SdmMixin
								
				def object = mainClass.newInstance()
				mainInstanceMap[key] = object
				
				object.metaClass {
					moduleManager = this
					delegate.mcl = mcl
					delegate.serviceRegistry = this.serviceRegistry
					basePackage = resolveModuleBasePackage(resolvedDep)
				}
				
				try {
					notifyModuleStarting dep: resolvedDep, key: key, object: object
				} catch(Exception e) {
					Log.info "Exception in notifyModuleStarting: $e"
				}
				try {
					object.invokeMethod 'run', null			
				} catch(Exception e) {
					Log.info "Exception in $resolvedDep ModuleMain: $e"
					//deps()
				} 
                try {
                    notifyModuleStarted dep: resolvedDep, key: key, object: object
                } catch(Exception e) {
                    Log.info "Exception in notifyModuleStarted: $e"
                }
				
			} catch(ClassNotFoundException e) {
				Log.trace("Module $resolvedDep doesn't have a main class.");
			} 
			
			mcl.start()	
			
			long dur = System.currentTimeMillis() - now
			Log.info("...started in $dur ms. $resolvedDep")
		} finally {
			//restore context classloader
			Thread.currentThread().contextClassLoader = loader
		}		
		mcl
	}
	
	def stopModule(String dep) {
		stopModule depFormat.parse(dep)
	}
	
	def stopModule(Map dep) {
		def md = resolveDependency(dep)
		stopModule md
	}
	
	def stopModule(ModuleDescriptor md) {
		long now = System.currentTimeMillis()
		def dep = md.moduleDep
		
		Log.info("Stopping module $dep ...")
		
		if (!isModuleStarted(md)) {
			Log.trace("Cannot stop a module that is not started!")
			return
		}
		
		def key = depFormat.toString(dep) as String
		def mcl = getMcl(dep)
		assert mcl
		
		mcl.stop()
		
		try {
			def object = mainInstanceMap[key]
			object?.stop()
		} catch(MissingMethodException e) {
			Log.trace "Module $dep doesn't have a stop method in its ModuleMain."
		} 		
		
		mcl.finalize()
		mclMap.remove key
		mainInstanceMap.remove key	
		
		long dur = System.currentTimeMillis() - now
		Log.info("Module $dep stopped in $dur ms.")
	}
	
	def restartModule(String dep) {
		restartModule depFormat.parse(dep)
	}
	
	def restartModule(Map dep) {
		def md = resolveDependency(dep)
		restartModule md
	}
	
	def restartModule(ModuleDescriptor md) {
		if (isModuleStarted(md)) {
			stopModule md
		}
		startModule md
	}
	
	boolean isModuleStarted(ModuleDescriptor md) {
		def dep = md.moduleDep
		def key = depFormat.toString(dep)
		mclMap.containsKey key
	}
	
	def assureModuleStarted(Map dep) {
		def md = resolveDependency(dep)
		assureModuleStarted md
	}
	
	def assureModuleStarted(ModuleDescriptor md) {
		if (!isModuleStarted(md)) {
			startModule md
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
    * Notify modules that a module is started
    * @param args
    * @return
    */
   def notifyModuleStarted(Map args) {
       mainInstanceMap.each { k,v ->
           try {
                k != args.key && v.onModuleStarted(args)
           } catch(MissingMethodException e) {}
       }
   }
    
	/**
	 * Notify the required module
	 * @param args
	 * @return
	 */
	def notifyModuleRequired(Map args) {
		def loader = Thread.currentThread().contextClassLoader
		def instance = getModuleMainInstance(args.requiredDep)
		if (instance) {
			try {					
				assert instance.mcl
				//Thread.currentThread().contextClassLoader = instance.mcl
				instance.onRequire args
			} catch(MissingMethodException e) {
			
			} finally {
				Thread.currentThread().contextClassLoader = loader
			}
		}		
	}
	
	ModuleDescriptor resolveDependency(Map dep) {
		dependencyResolver.resolveDependency dep
	}	
	
	def addDependency(mcl, Map dep, caller) {
		def md = resolveDependency(dep)
		addDependency mcl, md, caller
	}
	
	def addDependency(mcl, ModuleDescriptor md, caller) {
		def dep = md.moduleDep
		mcl.addDependency md
		//unique (ignoring version)
		mcl.moduleDeps.unique { a,b -> a.module == b.module && a.group == b.group ? 0 : 1 }
		
		assureModuleStarted md								
		notifyModuleRequired requiringDep: mcl.moduleDep, requiredDep: dep, requiringObject: caller				
	}
			
	def list() {
		mclMap.each { key,mcl -> 
			println "$key (${mcl.allLoadedClasses.size()} classes)"
		}
	}
	
	def listClasses() {
		mclMap.each { key,mcl -> 
			println "\n$key (${mcl.allLoadedClasses.size()} classes)"
			mcl.allLoadedClasses.each { c ->
				println "\t$c"
			}
		}
	}
	
	def listModules() {
		def results = []
		mclMap.each { key,mcl -> 
			results << "$key (${mcl.allLoadedClasses.size()} classes)"
		}             
		results
	}
	
	def deps() {
		mclMap.each { key,mcl -> 
			println "$key (${mcl.allLoadedClasses.size()} classes):"
			println '   Used:'
			mcl.loadedDeps.each { dep ->
				println "   $dep"
			}
			println '   Started:'
			mcl.startedDeps.each { dep ->
				println "   $dep"
			}
			println '   Declared:'
			mcl.moduleDeps.each { dep ->
				println "   $dep"
			}			
			println ''
		}
	}
	
	def dump() {
		//def parentPkgs = getClass().getClassLoader().packages
		
		mclMap.each { key,mcl -> 
			def urls = mcl.getURLs()
			def classes = mcl.allLoadedClasses
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


