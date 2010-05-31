package org.sdm.core;

class Service {
	
	private final static ServiceRegistry instance = new ServiceRegistry()

	static register(String name, bean) {
		instance.register name, bean
	}
	
	static lookup(String name) {
		instance.lookup name
	}
	
	static class ServiceRegistry {
		
		Map services = [:]
		
		def register(name, bean) {
			services[name] = bean
		}
		
		def lookup(name) {
			services[name]
		}
	}
}
