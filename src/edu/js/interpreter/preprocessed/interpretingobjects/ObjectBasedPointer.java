package edu.js.interpreter.preprocessed.interpretingobjects;

public class ObjectBasedPointer<T> implements Pointer<T> {
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
