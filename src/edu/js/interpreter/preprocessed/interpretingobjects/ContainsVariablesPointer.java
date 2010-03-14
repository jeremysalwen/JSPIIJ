package edu.js.interpreter.preprocessed.interpretingobjects;

import edu.js.interpreter.preprocessed.interpretingobjects.variables.ContainsVariables;

public class ContainsVariablesPointer implements Pointer {
	private ContainsVariables container;
	private String index;

	public ContainsVariablesPointer(ContainsVariables container, String index) {
		this.container = container;
		this.index = index;
	}

	@Override
	public Object get() {
		return container.get_var(index);
	}

	@Override
	public void set(Object value) {
		container.set_var(index, value);
	}

}