package org.sdm.core.dsl

import org.sdm.core.SDM;

class ConfigMixin {
	
	static final SDM_VERSION = SDM.VERSION
	
	def config = new Configuration()
	
	def configuration(clos) {		
		clos.delegate = config
		clos()
		config
	}
}
