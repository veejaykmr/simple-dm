package org.sdm.http;

import org.mortbay.io.ByteArrayEndPoint;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.LocalConnector;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.HandlerWrapper;

/**
 * This class dispatches requests coming from a servlet container to Jetty server handlers
 *  
 * @author alex
 *
 */
class JettyAdapter {
	
	def server
	
	def ccl
	
	def dispatch(path, req, resp) {
		def connector = server.connectors[0]
		def connection = new HttpConnectionAdapter(connector, new ByteArrayEndPoint(), server)
		HttpConnectionAdapter.setCurrentConnection connection
		
		def contextPath = req.contextPath
		
		def requestWrapper = new JettyRequestWrapper(req, path, connection)		
		def responseWrapper = new JettyResponseWrapper(resp, connection)
		
		connection.requestWrapper = requestWrapper
		connection.responseWrapper = responseWrapper
		
		requestWrapper.adapterPath = contextPath 
				
		server.handle path, requestWrapper, responseWrapper, 1
	}

}
