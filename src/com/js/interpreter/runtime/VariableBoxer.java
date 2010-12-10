package com.js.interpreter.runtime;

public interface VariableBoxer<T> {

	public abstract void set(T value);

	public abstract T get();

}