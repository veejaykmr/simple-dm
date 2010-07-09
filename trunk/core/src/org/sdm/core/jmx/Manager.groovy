package org.sdm.core.jmx

import org.sdm.core.Module;
import org.sdm.core.Starter;

class Manager implements ManagerMBean {
	
	String[] list() {
		Module.instance().listModules() as String[]
	}
	
	void start(String dep) {
		Starter.startNonInteractive(dep)
	}
}
