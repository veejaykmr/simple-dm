package org.simpledm.camel

require 'org.apache.camel:camel-spring:2.2.0'
	
org.simpledm.camel.dsl.CamelMixin.mixin org.simpledm.core.SdmMixin

def onModuleStarting(ctx) {
	println "Module org.simpledm:camel received onModuleStarting event for module: ${ctx.dep}"
}

def onRequire(ctx) {
	def object = ctx.requiringObject
	object instanceof GroovyObject && object.metaClass.mixin(org.simpledm.camel.dsl.CamelMixin)	
}

