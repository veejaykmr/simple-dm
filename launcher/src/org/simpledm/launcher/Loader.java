package org.simpledm.launcher;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Loader {
	public static void main(String[] args) throws Exception {
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		
		Properties props = new Properties(); 		
		InputStream is = parent.getResourceAsStream("sdm.properties");			
		props.load(is);		
				
		String version = props.getProperty("version");
		
		String baseDir = System.getProperty("m2.repo", System.getProperty("localRepository", System.getProperty("user.home")+ "/.m2/repository"));	
		URI baseDirUri = new File(baseDir).toURI();
	
		String[] jars = {"org/codehaus/groovy/groovy-all/1.7.2/groovy-all-1.7.2.jar",
			"org/apache/ivy/ivy/2.1.0/ivy-2.1.0.jar",
			"org/simpledm/sdm-core/"+ version +"/sdm-core-"+ version +".jar",
			"org/simpledm/sdm-config/"+ version +"/sdm-config-"+ version +".jar",
			"org/simpledm/maven/sdm-mvn-provider/"+ version +"/sdm-mvn-provider-"+ version +".jar",
			"org/simpledm/maven/sdm-mvn-metadata/"+ version +"/sdm-mvn-metadata-"+ version +".jar"};
		
		List<URL> urls = new ArrayList<URL>(); 
		for (String jar : jars){
			urls.add(new URL(baseDirUri.toString() + jar));
		}

		URLClassLoader classloader = new URLClassLoader(urls.toArray(new URL[urls.size()]), parent);
		Thread.currentThread().setContextClassLoader(classloader);
		
		//Starter.main(args);
		Class starter = classloader.loadClass("org.simpledm.core.Starter");
		Method m = starter.getMethod("main", String[].class);
		m.invoke(null, new Object[]{args});
	}
}