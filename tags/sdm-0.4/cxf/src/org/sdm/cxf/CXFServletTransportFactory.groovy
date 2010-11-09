package org.sdm.cxf


import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.servlet.ServletController;
import org.apache.cxf.transport.servlet.ServletDestination;
import org.apache.cxf.wsdl.http.AddressType;

import java.util.Set;

import org.apache.cxf.Bus;
import org.apache.cxf.transport.DestinationFactory;
import org.apache.cxf.transport.http.AbstractHTTPTransportFactory;
import org.apache.cxf.transport.servlet.ServletTransportFactory;

class CXFServletTransportFactory extends ServletTransportFactory {
	
	protected Map<String, ServletDestination> _destinations = new ConcurrentHashMap<String, ServletDestination>();
	
	protected Map<String, ServletDestination> _decodedDestinations = new ConcurrentHashMap<String, ServletDestination>();
	
	ServletController controller;
	
	public CXFServletTransportFactory(Bus b) {
		super.setBus(b);
	    List<String> ids = ["http://cxf.apache.org/transports/servlet"]
	    this.setTransportIds(ids);     
	}
	
	public CXFServletTransportFactory() {
	}
	
	@Override
	public Set<String> getUriPrefixes() {
		return ['servlet://'] as Set
	}
	
	@Override
	public void setServletController(ServletController c) {
		super.setServletController c
		controller = c;
	}
	
	@Override
	public void removeDestination(String path) throws IOException {
		_destinations.remove(path);
		_decodedDestinations.remove(URLDecoder.decode(path, "ISO-8859-1"));
	}
	
	@Override
	public Destination getDestination(EndpointInfo endpointInfo)
	throws IOException {
		ServletDestination d = getDestinationForPath(endpointInfo.getAddress());
		if (d == null) { 
			String path = getTrimmedPath(endpointInfo.getAddress());
			d = new ServletDestination(getBus(), this, endpointInfo, this, path);
			_destinations.put(path, d);
			_decodedDestinations.put(URLDecoder.decode(path, "ISO-8859-1"), d);
			
			if (controller != null && !StringUtils.isEmpty(controller.getLastBaseURL())) {
				String ad = d.getEndpointInfo().getAddress();
				if (ad != null && (ad.equals(path) || ad.equals(controller.getLastBaseURL() + path))) {
					d.getEndpointInfo().setAddress(controller.getLastBaseURL() + path);
					if (d.getEndpointInfo().getExtensor(AddressType.class) != null) {
						d.getEndpointInfo().getExtensor(AddressType.class).setLocation(controller.getLastBaseURL() + path);
					}
				}
			}
		}
		return d;
	}
	
	@Override
	public ServletDestination getDestinationForPath(String path, boolean tryDecoding) {
		// to use the url context match  
		String m = getTrimmedPath(path);
		ServletDestination s = _destinations.get(m);
		if (s == null) {
			s = _decodedDestinations.get(m);
		}
		return s;
	}
	
	String getTrimmedPath(String path) {
		if (path == null) {
			return "/";
		}
		final String lh = "servlet://localhost/";
		
		if (path.startsWith(lh)) {
			path = path.substring(lh.length());
		}
		
		if (!path.startsWith("/")) {
			path = "/" + path;
			
		}
		return path;
	}
	
	@Override
	public Collection<ServletDestination> getDestinations() {
		return Collections.unmodifiableCollection(_destinations.values());        
	}
	
	@Override
	public Set<String> getDestinationsPaths() {
		return Collections.unmodifiableSet(_destinations.keySet());        
	}
	
}
