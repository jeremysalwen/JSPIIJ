package com.js.interpreter.plugins;

import java.lang.reflect.Array;
import java.util.Map;

import com.js.interpreter.ast.PascalPlugin;
import com.js.interpreter.plugins.annotations.ArrayBoundsInfo;
import com.js.interpreter.plugins.annotations.MethodTypeData;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class Misc_plugins implements PascalPlugin {
	
	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }) })
	public static long GetArrayLength(Object[] o) {
		return o.length;
	}

	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }),
			@ArrayBoundsInfo })
	public static void SetArrayLength(VariableBoxer<Object[]> a, int length) throws RuntimePascalException {
		Object[] old = a.get();
		Class c = old.getClass().getComponentType();
		Object[] result = (Object[]) Array.newInstance(c, length);
		System.arraycopy(old, 0, result, 0, old.length);
		a.set(result);
	}

	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }),
			@ArrayBoundsInfo })
	public static void setLength(VariableBoxer<Object[]> a, int length) throws RuntimePascalException {
		SetArrayLength(a,length);
	}
	
	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }) })
	public static int length(Object[] o) {
		return o.length;
	}

	@Override
	public boolean instantiate(Map<String, Object> pluginargs) {
		return true;
	}
}
