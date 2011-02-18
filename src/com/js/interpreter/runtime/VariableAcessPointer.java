package com.js.interpreter.runtime;

import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.SubvarIdentifier;

public class VariableAcessPointer<T> implements VariableBoxer<T> {
	Object container;
	SubvarIdentifier index;

	public VariableAcessPointer(Object container, SubvarIdentifier index) {
		this.container = container;
		this.index = index;
	}

	@Override
	public T get() throws RuntimePascalException {
		return (T) index.get(container, null, null);
	}

	@Override
	public void set(T value) {

	}

	@Override
	public VariableAcessPointer<T> clone() {
		return new VariableAcessPointer<T>(container, index);
	}
}
