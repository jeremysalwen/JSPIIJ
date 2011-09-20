package com.js.interpreter.plugins.standard;

import java.util.Map;

import com.js.interpreter.plugins.PascalPlugin;

public class Conversion_plugins implements PascalPlugin {
	public static String BoolToStr(boolean b) {
		return b ? "True" : "False";
	}

	public static StringBuilder IntToStr(int i) {
		return new StringBuilder(String.valueOf(i));
	}

	public static String FloatToStr(double f) {
		return String.valueOf(f);
	}

	public static String CharToStr(char c) {
		return String.valueOf(c);
	}

	public static int strtoint(String s) {
		return strtointdef(s, -1);
	}

	public static int strtointdef(String s, int i) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return i;
		}
	}

	public static char chr(int i) {
		return (char) i;
	}

	@Override
	public boolean instantiate(Map<String, Object> pluginargs) {
		return true;
	}
}
