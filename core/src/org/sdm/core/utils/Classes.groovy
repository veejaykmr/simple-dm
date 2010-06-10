package org.sdm.core.utils;

class Classes {
	static new_(className) {
		Thread.currentThread().contextClassLoader.loadClass(className).newInstance()
	}
}
