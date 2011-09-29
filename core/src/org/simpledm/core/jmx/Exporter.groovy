package org.simpledm.core.jmx

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

class Exporter {
	
	MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	
	Exporter(mbean, name) {		
		mbs.registerMBean(mbean, name);
	}	

}
