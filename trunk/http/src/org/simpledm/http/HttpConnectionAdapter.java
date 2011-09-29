package org.simpledm.http;

import org.mortbay.io.EndPoint;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Response;
import org.mortbay.jetty.Server;

public class HttpConnectionAdapter extends HttpConnection {
	
	JettyRequestWrapper requestWrapper;
	
	JettyResponseWrapper responseWrapper;

	public HttpConnectionAdapter(Connector connector, EndPoint endpoint, Server server) {
		super(connector, endpoint, server);
	}

	public static void setCurrentConnection(HttpConnection connection) {
		HttpConnection.setCurrentConnection(connection);
	}

	public JettyRequestWrapper getRequestWrapper() {
		return requestWrapper;
	}

	public void setRequestWrapper(JettyRequestWrapper requestWrapper) {
		this.requestWrapper = requestWrapper;
	}

	public JettyResponseWrapper getResponseWrapper() {
		return responseWrapper;
	}

	public void setResponseWrapper(JettyResponseWrapper responseWrapper) {
		this.responseWrapper = responseWrapper;
	}

	@Override
	public Request getRequest() {
		return requestWrapper;
	}
	
	@Override
	public Response getResponse() {
		return responseWrapper;
	}

}
