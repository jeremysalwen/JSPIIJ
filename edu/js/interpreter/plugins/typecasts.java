package edu.js.interpreter.plugins;

import edu.js.interpreter.processing.pascal_plugin;

public class typecasts implements pascal_plugin {
	public static int integer(double d) {
		return (int) d;
	}

	public static int integer(long l) {
		return (int) l;
	}

	public static double real(long l) {
		return (double) l;
	}

	public static double real(int i) {
		return (double) i;
	}

	public static String tostring(int i) {
		return String.valueOf(i);
	}

	public static String tostring(double d) {
		return String.valueOf(d);
	}

	public static long longint(int i) {
		return (long) i;
	}

	public static long longint(double d) {
		return (long) d;
	}
}
