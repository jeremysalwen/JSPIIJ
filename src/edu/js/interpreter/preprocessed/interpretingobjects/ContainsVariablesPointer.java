package edu.js.interpreter.preprocessed.interpretingobjects;

import edu.js.interpreter.preprocessed.interpretingobjects.variables.ContainsVariables;

public class ContainsVariablesPointer<T> extends Pointer<T> {
	ContainsVariables v;

	String variable_name;

	public ContainsVariablesPointer(ContainsVariables v, String name) {
		this.v = v;
		this.variable_name = name;
	}

	@Override
	public T get() {
		return (T) v.get_var(variable_name);
	}

	@Override
	public void set(T value) {
		v.set_var(variable_name, value);
	}

}
