package org.sdm.maven.provider;

public class Log {
	public static boolean trace = false;
	
	public static boolean info = true;
	
	public static int depth = 1;
	
	public static void trace(String msg) {
		if (trace)
			System.out.println(msg);
	}
	
	public static void info(String msg) {
		if (info)
			System.out.println(msg);
	}
}

	