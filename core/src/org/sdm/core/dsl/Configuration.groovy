package org.sdm.core.dsl;

class Configuration {	
	
	def projects = []
	
	def addProject() {
		def result = new Project() 
		projects << result
		result
	}
	
	Project getProject(moduleKey) {
		projects.find { it.moduleKey == moduleKey }
	}

}
