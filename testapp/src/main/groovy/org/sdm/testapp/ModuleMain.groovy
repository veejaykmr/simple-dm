package org.sdm.testapp;

import org.apache.camel.spi.RouteContext;
import org.apache.camel.spring.SpringCamelContext;
import org.sdm.core.Module;
import org.sdm.testapp.DebugProcessor;
import org.sdm.testapp.HelloServiceImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.camel.builder.RouteBuilder

class ModuleMain {
	
	List dependencies = [[group: 'org.sdm', module: 'cxf', revision: Module.VERSION, scope: 'runtime']]
	
	Map aliases = [
	   'org.mortbay.jetty:servlet-api:2.5-20081211': [group: 'org.apache.geronimo.specs', module:'geronimo-servlet_2.5_spec', revision: '1.2']
	]
	
	def camel
	
	def context
		
	def start() {
 		println ">>>>>>> Module starting!!!"			
		init()		
	}

	def stop() {
		println ">>>>>>>>>>>>>>>>>> Shutdown module"
	}

	def init() {
		println "start"
		
		context = new ClassPathXmlApplicationContext("org/sdm/testapp/application-context.xml")
		
		camel = new SpringCamelContext(context)
		
		def debugProc = new DebugProcessor()
		
		camel.addRoutes(new RouteBuilder() {
			public void configure() {
				// Here we just pass the exception back , don't need to use errorHandler
				errorHandler(noErrorHandler());
				from('cxfrs://bean://rsServer').process(debugProc).to('bean://helloService');
			}
		});
		
		camel.start()
		
		println 'done'
	}
}
