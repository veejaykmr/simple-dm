package org.sdm.cxf

import java.lang.reflect.Field;
import java.util.List;

import org.apache.camel.spi.RouteContext;
import org.apache.camel.spring.SpringCamelContext;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.DefaultHandler;
import org.sdm.core.Service;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.camel.builder.RouteBuilder
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngineFactory;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngine;

class ModuleMain {
	
	List runtimeDependencies = [[group: 'org.sdm', module: 'http', revision: '0.2-SNAPSHOT']]
	
	def context
	
	def start() {
		init()
	}
	
	def stop() {
		
	}
	
	def init() {
		println "start"
		
		Server server = Service.lookup('http.server')
		assert server
		Connector connector = server.getConnectors()[0]
		assert connector
		
		context = new ClassPathXmlApplicationContext("org/sdm/cxf/application-context.xml")
		def bus = context.getBean("cxf")
		
		JettyHTTPServerEngineFactory serverEngineFactory = new JettyHTTPServerEngineFactory();
		serverEngineFactory.bus = bus
		serverEngineFactory.finalizeConfig();
		
		JettyHTTPServerEngine engine = serverEngineFactory.createJettyHTTPServerEngine(connector.host, connector.port, 'http')
		engine.setServer(server)
		engine.setSessionSupport true
		
		def contexts = new ContextHandlerCollection();
		server.addHandler(contexts);
		contexts.start()
		
		def handler = new DefaultHandler()
		server.addHandler handler
		handler.start()
				
		Field privateField = JettyHTTPServerEngine.class.getDeclaredField("contexts");
		privateField.setAccessible(true);		
		privateField.set(engine, contexts)
		
		Service.register 'cxf', bus
		
		println 'done'
	}
}
