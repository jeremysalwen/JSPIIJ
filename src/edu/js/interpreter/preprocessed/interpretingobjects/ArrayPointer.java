package edu.js.interpreter.preprocessed.interpretingobjects;

import java.lang.reflect.Array;

public class ArrayPointer<T> extends Pointer<T> {
	Object array;

	int index;

	public ArrayPointer(Object var_holder, int i) {
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
