package org.simpledm.config

import org.simpledm.config.dsl.ConfigMixin;

def onRequire(ctx) {
	ctx.requiringObject.metaClass.mixin(ConfigMixin)	
}