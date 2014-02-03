package org.simpledm.core.utils;

class FileUtils {
	
	static list(String root) {
		list new File(root)
	}
	
	static list(File root) {
		def files = root.listFiles()
		files.collect { it.isDirectory() ? list(it) : it }.flatten()
	}

}
