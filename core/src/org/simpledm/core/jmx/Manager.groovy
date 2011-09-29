package org.simpledm.core.jmx

class Manager implements ManagerMBean {
	
	def moduleManager
	
	String[] list() {
		moduleManager.listModules() as String[]
	}
	
	void start(String dep) {
		moduleManager.startModule dep
	}
	
	void stop(String dep) {
		moduleManager.stopModule dep
	}
	
	void restart(String dep) {
		moduleManager.restartModule dep
	}
}
