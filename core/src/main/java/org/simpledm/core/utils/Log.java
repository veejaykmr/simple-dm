package org.simpledm.core.utils;

public class Log {
	public static boolean trace = false;
	
	public static boolean info = true;
	
	public static int depth = 1;
	
	public static void trace(String msg) {
		if (trace)
			System.out.println(Utils.asTab(depth-1) + msg);
	}
	
	public static void info(String msg) {
		if (info)
			System.out.println(Utils.asTab(depth-1) + msg);
	}
}

	