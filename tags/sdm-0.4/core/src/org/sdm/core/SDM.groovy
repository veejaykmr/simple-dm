package org.sdm.core

class SDM {
	
	static final VERSION = '0.4'
		
	static init() {
		ServiceLocator.initialize()
	}
		
	static getService(String name) {
		ServiceLocator.instance().serviceRegistry.lookup name
	}
}
