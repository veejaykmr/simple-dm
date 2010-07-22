package org.sdm.core.jee

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdm.core.ServiceLocator;
import org.sdm.core.Starter;

class SDMServlet extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		def dispatcher = ServiceLocator.instance().serviceRegistry.lookup('http.dispatcher')
		assert dispatcher
		dispatcher.dispatch req.pathInfo, req, resp
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		def value = config.getInitParameter('modules') ?: ''
		def modules = value.split(',') as List
		
		ServiceLocator.initialize()
		def starter = new Starter()
		
		modules.each { starter.start it as String}				
	}
	
}
