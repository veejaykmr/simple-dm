package org.sdm.core

class SDM {
	static final VERSION = '0.3-SNAPSHOT'
		
	static getService(String name) {
		ServiceLocator.instance().serviceRegistry.lookup name
	}
}
