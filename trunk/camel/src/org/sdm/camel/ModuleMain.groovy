package org.sdm.camel

require group: 'org.apache.camel', module: 'camel-spring', revision: '2.2.0'
	
org.sdm.camel.dsl.CamelMixin.mixin org.sdm.core.SdmMixin

def onModuleRequire(ctx) {
	def object = ctx.requiringObject
	object instanceof GroovyObject && object.metaClass.mixin(org.sdm.camel.dsl.CamelMixin)	
}

