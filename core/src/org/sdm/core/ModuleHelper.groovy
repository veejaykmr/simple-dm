package org.sdm.core

class ModuleHelper {
	
	def moduleMain
	
	ModuleHelper(moduleMain) {
		this.moduleMain = moduleMain
	}
	
	def getRuntimeDependencies() {
		def result
		try {
			result = moduleMain.dependencies.findAll { it.scope == 'runtime' }
		} catch(MissingPropertyException e) {
			result = []
		}
		result
	}
	
	def getStaticDependencies() {
		def result
		try {
			result = moduleMain.dependencies.findAll { it.scope != 'runtime' }
		} catch(MissingPropertyException e) {
			result = []
		}
		result
	}
	
}
