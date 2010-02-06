package edu.js.interpreter.plugins;

import java.lang.reflect.Array;
import edu.js.interpreter.plugins.annotations.ArrayBoundsInfo;
import edu.js.interpreter.plugins.annotations.MethodTypeData;
import edu.js.interpreter.preprocessed.interpretingobjects.Pointer;
import edu.js.interpreter.processing.PascalPlugin;

public class SCAR_Misc implements PascalPlugin {
	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }) })
	public long GetArrayLength(Object[] o) {
		return o.length;
	}

	@MethodTypeData(info = {
			@ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }),
			@ArrayBoundsInfo })
	public void SetArrayLength(Pointer<Object> a, int length) {
		Class c = a.get().getClass().getComponentType();
		a.set(Array.newInstance(c, length));
	}

	@MethodTypeData(info = { @ArrayBoundsInfo(starts = { 0 }, lengths = { 0 }) })
	public int length(Object[] o) {
		return o.length;
	}
}
