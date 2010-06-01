package org.sdm.core

class Loader {
	static void main(args) {
		def parent = Loader.class.getClassLoader()		
	
		def jars = new File('lib').list().findAll { it =~ /.*\.jar/ }
		assert jars
		def urls = jars.collect { new File("lib/$it").toURL() }
		def classloader = new URLClassLoader(urls as URL[], parent)
		
		Thread.currentThread().setContextClassLoader classloader
		
		Starter.main args
	}
}