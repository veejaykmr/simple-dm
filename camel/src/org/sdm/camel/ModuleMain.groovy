package org.sdm.camel

require group: 'org.apache.camel', module: 'camel-spring', revision: '2.2.0'

def onModuleStarting(dep) {
	def object = moduleManager.getModuleMainInstance(dep)
	if (object instanceof GroovyObject) {
		object.metaClass.mixin CamelDslMixin		
	}
}

