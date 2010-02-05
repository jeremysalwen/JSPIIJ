package edu.js.interpreter.preprocessed.interpreting_objects;

import edu.js.interpreter.preprocessed.interpreting_objects.variables.contains_variables;

public class containsvariablespointer<T> extends pointer<T> {
	contains_variables v;

	String variable_name;

	public containsvariablespointer(contains_variables v, String name) {
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
