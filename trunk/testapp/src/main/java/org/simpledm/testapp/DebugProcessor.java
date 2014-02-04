package org.simpledm.testapp;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class DebugProcessor implements Processor {

	public void process(Exchange exchange) throws Exception {
		System.out.println(">>>>>>>>>>>>>>> Debug processor");

		final Message in = exchange.getIn();
		String opName = in.getHeader("operationname", String.class);
		in.setHeader(Exchange.BEAN_METHOD_NAME, opName);
		in.setHeader(Exchange.BEAN_MULTI_PARAMETER_ARRAY, true);

	}

}
