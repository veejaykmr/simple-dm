package org.sdm.core;

import javax.management.ObjectName;

import org.sdm.core.dsl.ConfigBuilder;
import org.sdm.core.dsl.ConfigService;
import org.sdm.core.dsl.Configuration;
import org.sdm.core.jmx.Exporter;
import org.sdm.core.jmx.Manager;
import static org.sdm.core.utils.Classes.*;

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
		
		metadataProvider = new_('org.sdm.maven.provider.MetadataProvider')	
		
		engine = new ResolveEngine()
		
		configuration = new ConfigService().configuration		
		
		serviceRegistry = new ServiceRegistry()
		
		dependencyResolver = new DependencyResolver(resolveEngine: engine, configuration: configuration, 
				parentClassLoader: classloader)
		
		moduleManager = new ModuleManager(metadataProvider: metadataProvider, serviceRegistry: serviceRegistry, 
				serviceLocator: this, dependencyResolver: dependencyResolver)
		
		managerMBean = new Manager(moduleManager: moduleManager)
		
		// jmx export
		exporter = new Exporter(managerMBean, new ObjectName('org.sdm.jmx:type=ManagerMBean'))
		
		initialized = true
	}		
}


