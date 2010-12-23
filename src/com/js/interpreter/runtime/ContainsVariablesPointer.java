package com.js.interpreter.runtime;

import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public class ContainsVariablesPointer implements VariableBoxer {
	private ContainsVariables container;
	private String index;

	public ContainsVariablesPointer(ContainsVariables container, String index) {
		this.container = container;
		this.index = index;
	}

	@Override
	public Object get() throws RuntimePascalException {
		return container.get_var(index);
	}

	@Override
	public void set(Object value) {
		container.set_var(index, value);
	}

}
