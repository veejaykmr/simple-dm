package org.sdm.core;

import org.sdm.core.utils.FileUtils;

class ModuleObserver extends Thread {
		
	def mcl
	
	boolean stop
	
	ModuleObserver(mcl) {
		this.mcl = mcl
	}
		
	void run() {		
		while (!stop) {
			Thread.currentThread().sleep 1000
			
			def urls = mcl.getURLs()
			
			def files = urls.collect { FileUtils.list(it.path) }.flatten() 
			def modifiedFile = files.find { it.lastModified() > mcl.startingDate.time }
			if (modifiedFile) {
				Module.restartModule(mcl.moduleDep)					
			}
		}
	}	
	
}
