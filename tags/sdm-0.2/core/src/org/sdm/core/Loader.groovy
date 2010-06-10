package org.sdm.core

class Loader {
	static void main(args) {
		def parent = Loader.class.getClassLoader()		
	
		def jars = new File('lib').list().findAll { it =~ /.*\.jar/ }
		assert jars
		def urls = jars.collect { new File("lib/$it").toURL() }
		// add lib directory in the classpath
		urls << new File('lib/').toURL()

		def classloader = new URLClassLoader(urls as URL[], parent)
		Thread.currentThread().contextClassLoader = classloader
		
		Starter.main args
	}
}