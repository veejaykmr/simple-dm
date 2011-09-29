package org.simpledm.core.jee

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.simpledm.core.SDM;
import org.simpledm.core.ServiceLocator;
import org.simpledm.core.Starter;

class SDMServlet extends HttpServlet {
	
	def adapter
		
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		if (!adapter) {
			adapter = SDM.getService('http.adapter')
			assert adapter
		}
		
		try {
			adapter.dispatch req.pathInfo, req, resp
		} catch(Exception e) {
			System.err.println('>>>>>BEGIN stack trace')
			while(e) {
				System.err.println(e)
				e = e.cause
			}
			System.err.println('>>>>>END stack trace')
		}
		
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		def value = config.getInitParameter('modules') ?: ''
		value = value.trim()
		def modules = value ? value.split(',') as List : []
		
		ServiceLocator.initialize()
		def starter = new Starter()
		
		modules.each { starter.start it as String }			
	}
	
}
