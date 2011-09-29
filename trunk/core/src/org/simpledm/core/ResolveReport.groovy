package org.simpledm.core;

import java.net.URI;
import java.util.List;
import java.util.Map;

class ResolveReport {
	Collection<Map> moduleDeps
	
	Collection<URI> uris
	
	Map getModuleDep() { assert moduleDeps; moduleDeps.iterator().next() }
	
	URI getModuleUri() { assert uris; uris.iterator().next() } 
}
