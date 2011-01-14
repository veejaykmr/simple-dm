package org.sdm.core.jee

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdm.core.SDM;
import org.sdm.core.ServiceLocator;
import org.sdm.core.Starter;

/**
 * Provides a REST interface to the SDM runtime
 * Only the start command is supported for now.
 * Ex: http://myhost/mypath/sdmrest/eu.aclement:oauthdemo:0.1 
 * THe above URL will start the oauthdemo module
 * Only modules white listed can be loaded
 * @author alex
 *
 */
class SDMRestServlet extends HttpServlet {
	
	def moduleWhiteList

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String module = req.pathInfo
	
		module = module?.substring(1) // remove leading  '/'
		
		if (moduleWhiteList.contains(module)) {
			def starter = new Starter()
			starter.start module
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet req, resp
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		def value = config.getInitParameter('moduleWhiteList') ?: ''
		value = value.trim()
		moduleWhiteList = value ? value.split(',') as List : []
		
		ServiceLocator.initialize()	
	}
}
