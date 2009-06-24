package edu.js.interpreter.plugins;

import edu.js.interpreter.processing.pascal_plugin;

public class scar_math implements pascal_plugin {
	public static int ceil(double d) {
		return (int) Math.ceil(d);
	}

	public static int ceil(float d) {
		return ceil(d);
	}

	public static int floor(double d) {
		return (int) Math.floor(d);
	}

	public static int floor(float f) {
		return floor(f);
	}

	public static int round(double d) {
		return (int) Math.round(d);
	}

	public static int round(float f) {
		return Math.round(f);
	}
}
