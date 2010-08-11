package org.sdm.core.utils;

import java.net.URI;
import java.net.URL;

class Utils {
	static URL[] toURLs(List<URI> uris) {
		uris.collect { it.toURL() }
	}
	
	static String asTab(int n) {
		def result = ""
		n.times { result += '\t' }
		result
	}
}
