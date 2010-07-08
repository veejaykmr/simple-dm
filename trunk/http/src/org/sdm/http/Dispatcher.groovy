package org.sdm.http;

import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.HandlerWrapper;

import groovy.lang.MissingPropertyException;

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
