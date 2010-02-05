package edu.js.interpreter.preprocessed.interpreting_objects;

public class object_based_pointer<T> extends pointer<T> {
	public T obj;

	@Override
	public T get() {
		return obj;
	}

	@Override
	public void set(T value) {
		obj = value;
	}

}
