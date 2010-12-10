package com.js.interpreter.plugins;

import java.lang.reflect.Array;

import com.js.interpreter.ast.PascalPlugin;
import com.js.interpreter.plugins.annotations.ArrayBoundsInfo;
import com.js.interpreter.plugins.annotations.MethodTypeData;
import com.js.interpreter.runtime.VariableBoxer;

public class SCAR_Misc implements PascalPlugin {
	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }) })
	public long GetArrayLength(Object[] o) {
		return o.length;
	}

	@MethodTypeData(info = {
			@ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }),
			@ArrayBoundsInfo })
	public void SetArrayLength(VariableBoxer<Object> a, int length) {
		Class c = a.get().getClass().getComponentType();
		a.set(Array.newInstance(c, length));
	}

	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }) })
	public int length(Object[] o) {
		return o.length;
	}
}
