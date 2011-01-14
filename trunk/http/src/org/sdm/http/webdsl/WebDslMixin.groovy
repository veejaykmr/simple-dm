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
		
		boolean extractWAR = args.extractWAR ?: false
		
		def webCtx = new_('org.mortbay.jetty.webapp.WebAppContext')		
		webCtx.war = baseDirUrl.toExternalForm()
		webCtx.contextPath = contextPath
		webCtx.classLoader = mcl
		webCtx.extractWAR = extractWAR
				
		server.addHandler webCtx
				
		webapp = new WebApp(server: server, webCtx: webCtx)
		clos.resolveStrategy = Closure.DELEGATE_FIRST
		clos.delegate = webapp
		clos()
		
		webCtx.start()

		webapp
	}
}
