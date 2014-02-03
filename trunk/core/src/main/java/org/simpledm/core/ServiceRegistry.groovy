package org.simpledm.core;

class ServiceRegistry {
	
	Map services = [:]
	
	def register(name, bean) {
		services[name] = bean
	}
	
	def lookup(name) {
		services[name]
	}
}

