package edu.js.interpreter.preprocessed.interpreting_objects;

import java.lang.reflect.Array;

public class arraypointer<T> extends pointer<T> {
	Object array;

	int index;

	public arraypointer(Object var_holder, int i) {
		array = var_holder;
		index = i;
	}

	@Override
	public T get() {
		return (T) Array.get(array, index);
	}

	@Override
	public void set(T value) {
		Array.set(array, index, value);

	}

}
