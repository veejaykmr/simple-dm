package org.simpledm.core.dsl;

class ConfigService {
	
	def getConfiguration() {
		def result
		
		//load sdm config if any
		def loader = new GroovyClassLoader()
		def is = loader.getResourceAsStream("sdm-config.groovy")
		if (is) {			
			def scriptClass = loader.parseClass(is)
			scriptClass.mixin ConfigMixin			
									
			def script = scriptClass.newInstance()				
			result = script.invokeMethod('run', null)			
		} else {
			result = new Configuration()
		}
	}
}

