package org.simpledm.http

org.simpledm.http.webdsl.WebDslMixin.mixin org.simpledm.core.SdmMixin

String env = System.getenv('SDM_ENV') ?: 'dev'
	
def ccl = Thread.currentThread().getContextClassLoader()

def url = ccl.getResource('org/sdm/http/config.properties')
def config = new ConfigSlurper(env).parse(url)

server = new_('org.mortbay.jetty.Server')

def connectorClass = System.getProperty('org.simpledm.http.connector') ?: 
	config.server.connector ?: 'org.mortbay.jetty.nio.SelectChannelConnector'

def connector = new_(connectorClass)
connector.host = config.server.host
connector.port = config.server.port
		
server.addConnector connector		

def contexts = new_('org.simpledm.http.SdmContextHandlers')
server.addHandler(contexts);
		
def handler = new_('org.mortbay.jetty.handler.DefaultHandler')
server.addHandler handler
		
server.start()
		
serviceRegistry.register('http.server', server)

def adapter = new JettyAdapter(server: server, ccl: ccl)		
serviceRegistry.register('http.adapter', adapter)

println 'done'

def stop() {
	server.stop()
}

def onRequire(ctx) {
	def object = ctx.requiringObject
	object instanceof GroovyObject && object.metaClass.mixin(org.simpledm.http.webdsl.WebDslMixin)	
}

