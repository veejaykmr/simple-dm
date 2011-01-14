package eu.aclement.oauthdemo.services;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.http.HttpMessage;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

public class DebugProcessor implements Processor {

	public void process(Exchange exchange) throws Exception {		
		System.out.println(">>>>>>>>>>>>>>> Debug processor");	
		
		final Message in = exchange.getIn();		
		String opName = in.getHeader("operationname", String.class); 
		in.setHeader(Exchange.BEAN_METHOD_NAME, opName);
		in.setHeader(Exchange.BEAN_MULTI_PARAMETER_ARRAY, true);		
		
		if(in instanceof HttpMessage) {
			final HttpMessage httpIn = (HttpMessage) in;
			exchange.setProperty(AbstractHTTPDestination.HTTP_REQUEST, httpIn.getRequest());
			exchange.setProperty(AbstractHTTPDestination.HTTP_RESPONSE, httpIn.getResponse());
		}
	}

}
