package org.sdm.http

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.sdm.core.Service;

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
		
		def url = Thread.currentThread().getContextClassLoader().getResource('org/sdm/http/config.properties')
		def config = new ConfigSlurper(env).parse(url)
		
		server = new Server()
		def connector = new SelectChannelConnector()
		connector.host = config.server.host
		connector.port = config.server.port
				
		server.addConnector connector		
		
		server.start()
				
		Service.register('http.server', server)

		println 'done'
	}
}
