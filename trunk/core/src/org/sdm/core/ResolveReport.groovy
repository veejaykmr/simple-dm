package org.sdm.core;

import java.net.URI;
import java.util.List;
import java.util.Map;

class ResolveReport {
	List<Map> moduleDeps = []
	
	List<URI> uris
	
	Map getModuleDep() { moduleDeps.first() }
}
