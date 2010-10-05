package org.sdm.http.webdsl

import static org.sdm.core.utils.Classes.*;

class WebApp {
	
	def webCtx
	
	def servlets = [:]
	                
	def initParams(Map args) {
		webCtx.setInitParams args
	}
	
	def listener(Map args) {
		def className = args.className
		assert className
		def listener = new_(className)
		webCtx.addEventListener listener
	}
	
	def filter(Map args) {
		def filter = new_('org.mortbay.jetty.servlet.FilterHolder')
		
		def className = args.className
		assert className
		
		filter.className = className
		def name = args.name
		if(name)
			filter.name = name
		
		def url = args.url ?: '/*'		
		webCtx.addFilter filter, url, 0
	}
	
	def servlet(Map args, clos) {
		def result = servlet(args)
		clos.resolveStrategy = Closure.DELEGATE_FIRST
		clos.delegate = result
		clos()
		result
	}
	
	def servlet(Map args) {
		def className = args.className
		assert className
		
		def servlet = new_(className)
		
		def name = args.name ?: className
		
		def holder = new_('org.mortbay.jetty.servlet.ServletHolder')
		holder.servlet = servlet
		holder.name = name
				
		def url = args.url ?: '/*'	
				
		servlets[name] = servlet
		
		webCtx.addServlet holder, url
		
		servlet		
	}
	
	def stop() {
		webCtx.stop()
	}
}
