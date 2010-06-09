package org.sdm.core.utils;

class Classes {
	static create(className) {
		Thread.currentThread().contextClassLoader.loadClass(className).newInstance()
	}
}
