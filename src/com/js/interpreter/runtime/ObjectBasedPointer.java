package com.js.interpreter.runtime;

public class ObjectBasedPointer<T> implements VariableBoxer<T> {
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
