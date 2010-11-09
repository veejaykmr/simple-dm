package org.sdm.cxf

require group: 'org.sdm', module: 'http', revision: SDM_VERSION

context = new org.springframework.context.support.ClassPathXmlApplicationContext("org/sdm/cxf/application-context.xml")
def bus = context.getBean("cxf")
		
server = serviceRegistry.lookup('http.server')

if (server) {
	def connector = server.getConnectors()[0]
	assert connector			
	
	def serverEngineFactory = new_('org.apache.cxf.transport.http_jetty.JettyHTTPServerEngineFactory')
	serverEngineFactory.bus = bus
	serverEngineFactory.finalizeConfig()
	
	def engine = serverEngineFactory.createJettyHTTPServerEngine(connector.host, connector.port, 'http')
	engine.setServer(server)
	engine.setSessionSupport true
	
	def contexts = server.handler
			
	def privateField = engine.getClass().getDeclaredField("contexts");
	privateField.setAccessible(true);		
	privateField.set(engine, contexts)			
}

serviceRegistry.register 'cxf', bus

def stop() {
	
} 

println 'done'
