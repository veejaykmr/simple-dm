package org.sdm.core.dsl;

class ConfigService {
	
	def getConfiguration() {
		def result
		
		//load sdm config if any
		def loader = new GroovyClassLoader()
		def is = loader.getResourceAsStream("sdm-config.groovy")
		if (is) {
			def builder = new ConfigBuilder()
			def scriptClass = loader.parseClass(is)
			scriptClass.metaClass.configuration = { clos -> 
				clos.delegate = builder
				clos()
			}
			
			def script = scriptClass.newInstance()				
			script.invokeMethod('run', [] as Object[]);
							
			result = builder.build()
		} else {
			result = new Configuration()
		}
	}
}

