package edu.js.interpreter.preprocessed.interpretingobjects;

import java.lang.reflect.Array;

public class ArrayPointer implements Pointer {
	private int index;
	private Object container;

	boolean isString;

	public ArrayPointer(Object container, int index) {
		this.container = container;
		this.index = index;
		isString = container instanceof StringBuilder;
	}

	@Override
	public Object get() {
		if (isString) {
			return ((StringBuilder) container).charAt(index);
		} else {
			return Array.get(container, index);
		}
	}

	@Override
	public void set(Object value) {
		if (isString) {
			((StringBuilder) container).setCharAt(index, (Character) value);
		} else {
			Array.set(container, index, value);
		}
	}

}
