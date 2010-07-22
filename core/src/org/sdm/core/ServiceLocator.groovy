package org.sdm.core;

import javax.management.ObjectName;

import org.sdm.core.dsl.ConfigBuilder;
import org.sdm.core.dsl.ConfigService;
import org.sdm.core.dsl.Configuration;
import org.sdm.core.jmx.Exporter;
import org.sdm.core.jmx.Manager;
import static org.sdm.core.utils.Classes.*;

class ServiceLocator {
	
	static instance = new ServiceLocator()
	
	static instance() { instance }
	
	static initialize() { instance.init() }
	
	def classloader
	
	def resolver 
	
	CachedEngine engine 
	
	def configuration
	
	def managerMBean
	
	def exporter
	
	def serviceRegistry
	
	def moduleManager
	
	boolean initialized
	
	def moduleClassLoader(parentClassLoader, moduleDep) {
		def result = new ModuleClassLoader(parentClassLoader, moduleDep)
		result.moduleManager = moduleManager
		result.configuration = configuration
		result.init()
		result
	}
	
	def init() {
		if (initialized)
			return
		
		this.classloader = Thread.currentThread().contextClassLoader
		
		resolver = new_('org.sdm.maven.provider.MavenResolver')	
		
		engine = new CachedEngine(resolver: resolver)
		
		configuration = new ConfigService().configuration		
		
		serviceRegistry = new ServiceRegistry()
		
		moduleManager = new ModuleManager(parentClassLoader: classloader, resolver: resolver, engine: engine, 
				serviceRegistry: serviceRegistry, serviceLocator: this)
		
		managerMBean = new Manager(moduleManager: moduleManager)
		
		// jmx export
		exporter = new Exporter(managerMBean, new ObjectName('org.sdm.jmx:type=ManagerMBean'))
		
		initialized = true
	}		
}


