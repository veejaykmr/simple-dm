package org.sdm.http

import org.sdm.core.Service;
import static org.sdm.core.utils.Classes.*;

println "start"

String env = System.getenv('SDM_ENV') ?: 'dev'
	
def ccl = Thread.currentThread().getContextClassLoader()

def url = ccl.getResource('org/sdm/http/config.properties')
def config = new ConfigSlurper(env).parse(url)

server = new_('org.mortbay.jetty.Server')

def connectorClass = config.server.connector ?: 'org.mortbay.jetty.nio.SelectChannelConnector'

def connector = new_(connectorClass)
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

def stop() {
	server.stop()
}

