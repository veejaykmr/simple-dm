package org.simpledm.core

class ModuleDescriptor {
	
	Collection<URL> moduleUrls
	
	boolean developmentStage
	
	Collection<Map> moduleDeps
	                    	
	Collection<URI> uris	
	
	Map getModuleDep() { assert moduleDeps; moduleDeps.iterator().next() }
}
