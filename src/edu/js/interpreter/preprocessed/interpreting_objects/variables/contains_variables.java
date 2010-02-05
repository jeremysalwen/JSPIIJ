package edu.js.interpreter.preprocessed.interpreting_objects.variables;

public interface contains_variables {
	public Object get_var(String name);

	public void set_var(String name, Object val);

	public contains_variables clone();
}
