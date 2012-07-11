package org.simpledm.config

import org.simpledm.config.dsl.ConfigMixin;

def onRequire(ctx) {
	def object = ctx.requiringObject
	object instanceof GroovyObject && object.metaClass.mixin(ConfigMixin)	
}