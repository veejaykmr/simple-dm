package org.sdm.http

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.sdm.core.Service;
import static org.sdm.core.utils.Classes.*;

class ModuleMain { 
	
	def server
	
	def start() {
 		init()
	}

	def stop() {
		server.stop()
	}

	def init() {
		println "start"
		
		String env = System.getenv('SDM_ENV') ?: 'dev'
			
		def ccl = Thread.currentThread().getContextClassLoader()
		
		def url = ccl.getResource('org/sdm/http/config.properties')
		def config = new ConfigSlurper(env).parse(url)
		
		server = new Server()
		def connector = new SelectChannelConnector()
		connector.host = config.server.host
		connector.port = config.server.port
				
		server.addConnector connector		
		
		def contexts = new_('org.mortbay.jetty.handler.ContextHandlerCollection')
		server.addHandler(contexts);
				
		def handler = new_('org.mortbay.jetty.handler.DefaultHandler')
		server.addHandler handler
				
		server.start()
				
		Service.register('http.server', server)
		
		def dispatcher = new Dispatcher(server: server, ccl: ccl)		
		Service.register('http.dispatcher', dispatcher)

		println 'done'
	}
}
