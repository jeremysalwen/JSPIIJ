package edu.js.interpreter.plugins;

import edu.js.interpreter.processing.pascal_plugin;

public class scar_conversions implements pascal_plugin {
	public static String BoolToStr(boolean b) {
		return b ? "True" : "False";
	}

	public static String IntToStr(int i) {
		return String.valueOf(i);
	}

	public static String FloatToStr(float f) {
		return String.valueOf(f);
	}

	public static int strtoint(String s) {
		return strtointdef(s, -1);
	}

	public static int strtointdef(String s, int i) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return i; // Tested to be behavior
		}
	}

	public static char chr(int i) {
		return (char) i;
	}
}
