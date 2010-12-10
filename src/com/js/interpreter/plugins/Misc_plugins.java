package com.js.interpreter.plugins;

import java.lang.reflect.Array;

import com.js.interpreter.ast.PascalPlugin;
import com.js.interpreter.plugins.annotations.ArrayBoundsInfo;
import com.js.interpreter.plugins.annotations.MethodTypeData;
import com.js.interpreter.runtime.VariableBoxer;

public class Misc_plugins implements PascalPlugin {
	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }) })
	public long GetArrayLength(Object[] o) {
		return o.length;
	}

	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }),
			@ArrayBoundsInfo })
	public void SetArrayLength(VariableBoxer<Object[]> a, int length) {
		Object[] old = a.get();
		Class c = old.getClass().getComponentType();
		Object[] result = (Object[]) Array.newInstance(c, length);
		System.arraycopy(old, 0, result, 0, old.length);
		a.set(result);
	}

	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }) })
	public int length(Object[] o) {
		return o.length;
	}
}
