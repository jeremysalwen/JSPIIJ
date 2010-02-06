package edu.js.interpreter.preprocessed.interpretingobjects.variables;

public interface ContainsVariables {
	public Object get_var(String name);

	public void set_var(String name, Object val);

	public ContainsVariables clone();
}
