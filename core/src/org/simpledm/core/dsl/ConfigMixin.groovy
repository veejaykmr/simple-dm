package org.simpledm.core.dsl

import org.simpledm.core.SDM;

class ConfigMixin {
	
	static final SDM_VERSION = SDM.VERSION
	
	def config = new Configuration()
	
	def configuration(clos) {		
		clos.delegate = config
		clos.resolveStrategy = Closure.DELEGATE_FIRST
		clos()
		config
	}
}
