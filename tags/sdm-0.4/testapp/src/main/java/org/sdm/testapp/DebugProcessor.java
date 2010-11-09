package org.sdm.testapp;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DebugProcessor implements Processor {

	public void process(Exchange exchange) throws Exception {		
		System.out.println(">>>>>>>>>>>>>>> Debug processor");	
		
		String opName = exchange.getIn().getHeader("operationname", String.class); 
		exchange.getIn().setHeader(Exchange.BEAN_METHOD_NAME, opName);		
	}

}
