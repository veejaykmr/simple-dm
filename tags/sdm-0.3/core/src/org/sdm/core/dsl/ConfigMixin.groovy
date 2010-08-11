package org.sdm.core.dsl

import org.sdm.core.SDM;

class ConfigMixin {
	
	static final SDM_VERSION = SDM.VERSION
	
	def configuration(c) {
		def builder = new ConfigBuilder()
		c.delegate = builder
		c()
		builder.build()
	}
}
