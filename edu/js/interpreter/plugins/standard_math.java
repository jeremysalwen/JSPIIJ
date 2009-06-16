package edu.js.interpreter.plugins;

import edu.js.interpreter.processing.pascal_plugin;

public class standard_math implements pascal_plugin {
	public static double pow(double base, double exponent) {
		return Math.pow(base, exponent);
	}
}
