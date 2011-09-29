package org.simpledm.core;

import javax.management.ObjectName;

import org.simpledm.core.dsl.ConfigService;
import org.simpledm.core.dsl.Configuration;
import org.simpledm.core.jmx.Exporter;
import org.simpledm.core.jmx.Manager;
import static org.simpledm.core.utils.Classes.*;

class ServiceLocator {
	
	static final instance = new ServiceLocator()
	
	static instance() { instance }
	
	static initialize() { instance.init() }
	
	def classloader
	
	def metadataProvider 
	
	ResolveEngine engine 
	
	def configuration
	
	def managerMBean
	
	def exporter
	
	def serviceRegistry
	
	def dependencyResolver
	
	def moduleManager
		
	boolean initialized
	
	def moduleClassLoader(ModuleDescriptor md) {
		def dep = md.moduleDep
		def result = new ModuleClassLoader(classloader, dep)
		result.moduleManager = moduleManager
		result.configuration = configuration
		result.init(md)
		result
	}
	
	def init() {
		if (initialized)
			return
		
		this.classloader = Thread.currentThread().contextClassLoader
		
		metadataProvider = new_('org.simpledm.maven.provider.MetadataProvider')	
		
		engine = new ResolveEngine()
		
		configuration = new ConfigService().configuration		
		
		serviceRegistry = new ServiceRegistry()
		
		dependencyResolver = new DependencyResolver(resolveEngine: engine, configuration: configuration, 
				parentClassLoader: classloader)
		
		moduleManager = new ModuleManager(metadataProvider: metadataProvider, serviceRegistry: serviceRegistry, 
				serviceLocator: this, dependencyResolver: dependencyResolver)
		
		managerMBean = new Manager(moduleManager: moduleManager)
		
		// jmx export
		if (!configuration.disableJMX) {
			exporter = new Exporter(managerMBean, new ObjectName('org.simpledm:type=ManagerMBean'))
		}
		
		initialized = true
	}		
}


