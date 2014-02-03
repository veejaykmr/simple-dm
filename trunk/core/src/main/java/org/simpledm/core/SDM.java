package org.simpledm.core;

import java.io.InputStream;
import java.util.Properties;

public class SDM {

	public static final String VERSION;

	static {
		Properties props = new Properties(); 
		try {
			InputStream is = SDM.class.getClassLoader().getResourceAsStream("sdm.properties");			
			props.load(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		String version = props.getProperty("version");
		if (version == null)
			throw new RuntimeException("Impossible to determine SDM runtime version");
		VERSION = version;
	}

	public static void init() {
		ServiceLocator.initialize();
	}

	public static Object getService(String name) {
		return ServiceLocator.instance().getServiceRegistry().lookup(name);
	}
}
