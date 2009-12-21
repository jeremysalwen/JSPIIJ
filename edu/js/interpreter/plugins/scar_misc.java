package edu.js.interpreter.plugins;

import java.lang.reflect.Array;
import java.util.Arrays;

import edu.js.interpreter.preprocessed.interpreting_objects.pointer;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_misc implements pascal_plugin {
	public long GetArrayLength(Object[] o) {
		return o.length;
	}

	public void SetArrayLength(pointer<Object> a, int length) {
		Class c = a.get().getClass().getComponentType();
		a.set(Array.newInstance(c, length));
	}
	public int length(Object[] o) {
		return o.length; 
	}
}
