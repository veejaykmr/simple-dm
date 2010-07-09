package org.sdm.core.jmx

interface ManagerMBean {

	String[] list()
	
	void start(String dep)
}
