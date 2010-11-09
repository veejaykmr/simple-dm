package org.sdm.core;

import org.sdm.core.utils.FileUtils;

class ModuleObserver extends Thread {
	
	def moduleManager
		
	def mcl
	
	boolean stop
	
	ModuleObserver(mcl, moduleManager) {		
		this.mcl = mcl
		this.moduleManager = moduleManager
	}
		
	void run() {		
		def parentLoader = mcl.parent
		assert parentLoader
		Thread.currentThread().contextClassLoader = parentLoader
		
		while (!stop) {
			Thread.currentThread().sleep 1000
			
			def urls = mcl.getURLs()
			
			def files = urls.collect { FileUtils.list(it.path) }.flatten() 
			def modifiedFile = files.find { it.lastModified() > mcl.startingDate.time }
			if (modifiedFile) {				
				moduleManager.restartModule(mcl.moduleDep)					
			}
		}
	}	
	
}
