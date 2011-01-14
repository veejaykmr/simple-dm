package eu.aclement.oauthdemo.cxfbean;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.cxfbean.DefaultCxfBeanBinding;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.cxf.message.Message;
import static org.apache.cxf.transport.http.AbstractHTTPDestination.*;

public class HttpCxfBeanBinding extends DefaultCxfBeanBinding {
	
	@Override
	public Message createCxfMessageFromCamelExchange(Exchange camelExchange,
			HeaderFilterStrategy headerFilterStrategy) {
		Message result = super.createCxfMessageFromCamelExchange(camelExchange,
				headerFilterStrategy);
		
		result.put(HTTP_REQUEST, camelExchange.getProperty(HTTP_REQUEST));
		result.put(HTTP_RESPONSE, camelExchange.getProperty(HTTP_RESPONSE));
		
		return result;
	}

}
