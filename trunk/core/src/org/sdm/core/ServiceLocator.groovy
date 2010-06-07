package org.sdm.core;

class ServiceLocator {
	
	static instance = new ServiceLocatorImpl()
	
	static init() {
		instance.init()
	}
	
	static getClassLoader() {
		instance.classloader
	}
	
	static getResolver() {
		instance.resolver
	}
	
	static CachedEngine getCachedEngine() {
		instance.getCachedEngine()
	}
	
	static class ServiceLocatorImpl {
		
		def classloader
		
		def resolver 
		
		CachedEngine engine 
		
		def init() {
			this.classloader = Thread.currentThread().contextClassLoader
			resolver = classloader.loadClass('org.sdm.maven.provider.MavenResolver').newInstance()				
		}
		
		def getCachedEngine() {
			if (!engine) {
				engine = new CachedEngine()
			}
			engine
		}
	}
	
}
