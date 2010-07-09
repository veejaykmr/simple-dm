package org.sdm.cxf

import java.lang.reflect.Field;
import org.sdm.core.Module;
import org.sdm.core.Service;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static org.sdm.core.utils.Classes.*;

require group: 'org.sdm', module: 'http', revision: Module.SDM_VERSION
	
println "start"

context = new ClassPathXmlApplicationContext("org/sdm/cxf/application-context.xml")
def bus = context.getBean("cxf")
		
server = Service.lookup('http.server')

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
			
	Field privateField = engine.getClass().getDeclaredField("contexts");
	privateField.setAccessible(true);		
	privateField.set(engine, contexts)			
}

Service.register 'cxf', bus

def stop() {
	
} 

println 'done'
