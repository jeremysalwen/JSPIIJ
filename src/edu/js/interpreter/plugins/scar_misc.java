package edu.js.interpreter.plugins;

import java.lang.reflect.Array;
import edu.js.interpreter.plugins.annotations.array_bounds_info;
import edu.js.interpreter.plugins.annotations.method_type_data;
import edu.js.interpreter.preprocessed.interpreting_objects.pointer;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_misc implements pascal_plugin {
	@method_type_data(info = { @array_bounds_info(starts = { 0 }, lengths = { 0 }) })
	public long GetArrayLength(Object[] o) {
		return o.length;
	}

	@method_type_data(info = {
			@array_bounds_info(starts = { 0 }, lengths = { 0 }),
			@array_bounds_info })
	public void SetArrayLength(pointer<Object> a, int length) {
		Class c = a.get().getClass().getComponentType();
		a.set(Array.newInstance(c, length));
	}

	@method_type_data(info = { @array_bounds_info(starts = { 0 }, lengths = { 0 }) })
	public int length(Object[] o) {
		return o.length;
	}
}
