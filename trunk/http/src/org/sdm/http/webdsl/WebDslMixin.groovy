package org.sdm.http.webdsl

/**
 * 
 * This class defines a JEE Webapp DSL.
 * 
 * @author alex
 *
 */
class WebDslMixin {
	
	def webapp

	def web(Map args, clos) {		
		def server = serviceRegistry.lookup('http.server')
		assert server
		
		def war = args.war
		assert war
		
		def contextPath = args.path
		assert contextPath
						
		def baseDirUrl = mcl.getResource(war)
		assert baseDirUrl
		
		def webCtx = new_('org.mortbay.jetty.webapp.WebAppContext')		
		webCtx.war = baseDirUrl.toExternalForm()
		webCtx.contextPath = contextPath
		webCtx.classLoader = mcl
				
		server.addHandler webCtx
		
		clos.resolveStrategy = Closure.DELEGATE_FIRST
		
		webapp = new WebApp(server: server, webCtx: webCtx)
		clos.delegate = webapp
		clos()
		
		webCtx.start()

		webapp
	}
}
