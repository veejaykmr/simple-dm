package org.sdm.camel

require group: 'org.apache.camel', module: 'camel-spring', revision: '2.2.0'
	
org.sdm.camel.dsl.CamelMixin.mixin org.sdm.core.SdmMixin

def onModuleStarting(ctx) {
	println "Module org.sdm:camel received onModuleStarting event for module: ${ctx.dep}"
}

def onRequire(ctx) {
	def object = ctx.requiringObject
	object instanceof GroovyObject && object.metaClass.mixin(org.sdm.camel.dsl.CamelMixin)	
}

