package com.js.interpreter.plugins.standard;

import java.util.Map;

import com.js.interpreter.plugins.PascalPlugin;
import com.js.interpreter.plugins.annotations.ArrayBoundsInfo;
import com.js.interpreter.plugins.annotations.MethodTypeData;

public class Misc_plugins implements PascalPlugin {
	
	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }) })
	public static long GetArrayLength(Object[] o) {
		return o.length;
	}

	
	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }) })
	public static int length(Object[] o) {
		return o.length;
	}

	public static int length(StringBuilder s) {
		return s.length();
	}

	@Override
	public boolean instantiate(Map<String, Object> pluginargs) {
		return true;
	}
}
