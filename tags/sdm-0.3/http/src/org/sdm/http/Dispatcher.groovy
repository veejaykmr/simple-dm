package org.sdm.http;

import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.HandlerWrapper;

/**
 * This class dispatches requests coming from a servlet container to Jetty server handlers
 *  
 * @author alex
 *
 */
class Dispatcher {
	
	def server
	
	def ccl
	
	def dispatch(path, req, resp) {
		def requestWrapper = new JettyRequestWrapper(req, path)		
		
		def handlerCol = server.handler
				
		def handler = handlerCol.handlers.find { it instanceof ContextHandler }			
		assert handler
		
		while (handler instanceof HandlerWrapper) {
			handler = handler.handler
		}
		
		handler.handle path, requestWrapper, resp, 1
	}

}
