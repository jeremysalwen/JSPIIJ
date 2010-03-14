package edu.js.interpreter.preprocessed.interpretingobjects;

public interface Pointer<T> {

	public abstract void set(T value);

	public abstract T get();

}