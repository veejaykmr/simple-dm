package org.sdm.core.dsl;

import org.sdm.core.dsl.Project;

class ConfigBuilder {	
	
	def config = new Configuration()
	
	def project
	
	def project(attrs, elements) {
		project = config.addProject()
		
		project.moduleKey = attrs.module
		
		elements.delegate = this
		elements()		
	}
	
	def src(attrs) {
		assert project
		project.sources << attrs.path
	}
	
	def build() {
		config
	}
}