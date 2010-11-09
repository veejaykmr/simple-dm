package org.sdm.cxf

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.cxf.Bus;
import org.apache.cxf.resource.ResourceManager;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.servlet.AbstractCXFServlet;
import org.apache.cxf.transport.servlet.ServletContextResourceResolver;
import org.springframework.context.ApplicationEvent;

class SdmCxfServlet extends AbstractCXFServlet {
	
	@Override
	void loadBus(ServletConfig servletConfig) throws ServletException {
		ResourceManager resourceManager = bus.getExtension(ResourceManager.class);
		resourceManager.addResourceResolver(new ServletContextResourceResolver(servletConfig.getServletContext()));
		
		DestinationFactoryManager dfm = bus.getExtension(DestinationFactoryManager.class); 
		
		servletTransportFactory = dfm.getDestinationFactory("http://cxf.apache.org/transports/servlet");
		
		// Set up the ServletController
		controller = createServletController(servletConfig);
	}
	
	void setBus(Bus bus) {
		this.@bus = bus
	}
	
	@Override
	void destroy() {
		// don't destroy the bus
	}
}

