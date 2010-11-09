package org.sdm.core.utils;

public class Assert {
	
	public static void _assert(boolean expr, String msg) {
		if(!expr) {
			throw new IllegalArgumentException(msg);
		}
	}

}
