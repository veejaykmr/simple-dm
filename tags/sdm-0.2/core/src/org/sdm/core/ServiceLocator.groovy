package org.sdm.core;

import org.sdm.core.dsl.ConfigBuilder;
import org.sdm.core.dsl.Configuration;
import static org.sdm.core.utils.Classes.*;

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
		instance.engine
	}
	
	static Configuration getConfig() {
		instance.configuration
	}
	
	static class ServiceLocatorImpl {
		
		def classloader
		
		def resolver 
		
		CachedEngine engine 
		
		def configuration
		
		def init() {
			this.classloader = Thread.currentThread().contextClassLoader
			resolver = new_('org.sdm.maven.provider.MavenResolver')	
			engine = new CachedEngine()
			
			//load sdm config if any
			def loader = new GroovyClassLoader()
			def is = loader.getResourceAsStream("sdm-config.groovy")
			if (is) {
				def builder = new ConfigBuilder()
				def scriptClass = loader.parseClass(is)
				scriptClass.metaClass.configuration = { clos -> 
					clos.delegate = builder
					clos()
				}
				
				def script = scriptClass.newInstance()				
				script.invokeMethod('run', [] as Object[]);
								
				configuration = builder.build()
			}
		}		
	}
	
}
