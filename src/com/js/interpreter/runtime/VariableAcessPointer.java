package com.js.interpreter.runtime;

import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.SubvarIdentifier;

public class VariableAcessPointer implements VariableBoxer {
	Object container;
	SubvarIdentifier index;

	public VariableAcessPointer(Object container, SubvarIdentifier index) {
		this.container = container;
		this.index = index;
	}

	@Override
	public Object get() throws RuntimePascalException {
		return index.get(container, null, null);
	}

	@Override
	public void set(Object value) {

	}

}
