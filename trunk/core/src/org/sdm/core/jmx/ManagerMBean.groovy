package org.sdm.core.jmx

interface ManagerMBean {

	String[] list()
	
	void start(String dep)
	
	void stop(String dep)
	
	void restart(String dep)
}
