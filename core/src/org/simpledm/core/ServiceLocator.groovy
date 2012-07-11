package org.simpledm.core;

import javax.management.ObjectName;

import org.simpledm.core.config.Configuration;
import org.simpledm.core.jmx.Exporter;
import org.simpledm.core.jmx.Manager;
import static org.simpledm.core.utils.Classes.*;

class ServiceLocator {
	
	static final ServiceLocator instance = new ServiceLocator()
	
	static ServiceLocator instance() { instance }
	
	static initialize() { instance.init() }
	
	def classloader
	
	def metadataProvider 
	
	ResolveEngine engine 
		
	def managerMBean	
	
	ServiceRegistry serviceRegistry
	
	def dependencyResolver
	
	def moduleManager
		
	boolean initialized
	
	def moduleClassLoader(ModuleDescriptor md) {
		def dep = md.moduleDep
		def result = new ModuleClassLoader(classloader, dep)
		result.moduleManager = moduleManager		
		result.init(md)
		result
	}
	
	def init() {
		if (initialized)
			return
		
		this.classloader = Thread.currentThread().contextClassLoader
		
		metadataProvider = new_('org.simpledm.maven.provider.MetadataProvider')	
		
		engine = new ResolveEngine()
			
		serviceRegistry = new ServiceRegistry()
		
		dependencyResolver = new DependencyResolver(resolveEngine: engine, parentClassLoader: classloader)
		
		moduleManager = new ModuleManager(metadataProvider: metadataProvider, serviceRegistry: serviceRegistry, 
				serviceLocator: this, dependencyResolver: dependencyResolver)
		
		managerMBean = new Manager(moduleManager: moduleManager)
				
		initialized = true
	}
	
	void enableJMX() {
		new Exporter(managerMBean, new ObjectName('org.simpledm:type=ManagerMBean'))
	}
		
	Map getRootModuleDep(){
		def qualifier = System.getProperty('sdm.root.qualifier')
		def version = qualifier ? SDM.VERSION + '-' + qualifier : (System.getProperty('sdm.root.version') ?: SDM.VERSION)
		[group: 'org.simpledm', module: 'sdm-root', revision: version]
	}	
	
}


