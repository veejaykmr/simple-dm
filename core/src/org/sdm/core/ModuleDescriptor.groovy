package org.sdm.core

class ModuleDescriptor {
	
	List<URL> moduleUrls
	
	boolean developmentStage
	
	List<Map> moduleDeps
	                    	
	List<URI> uris	
	
	Map getModuleDep() { moduleDeps[0] }
}
