package org.sdm.core;

import javax.management.ObjectName;

import org.sdm.core.dsl.ConfigBuilder;
import org.sdm.core.dsl.ConfigService;
import org.sdm.core.dsl.Configuration;
import org.sdm.core.jmx.Exporter;
import org.sdm.core.jmx.Manager;
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
		
		def managerMBean
		
		def exporter
		
		boolean initialized
		
		def init() {
			if (initialized)
				return
			
			this.classloader = Thread.currentThread().contextClassLoader
			
			resolver = new_('org.sdm.maven.provider.MavenResolver')	
			
			engine = new CachedEngine()
			
			configuration = new ConfigService().configuration
			
			managerMBean = new Manager()
			
			// jmx export
			exporter = new Exporter(managerMBean, new ObjectName('org.sdm.jmx:type=ManagerMBean'))
					
			initialized = true
		}		
	}
	
}
