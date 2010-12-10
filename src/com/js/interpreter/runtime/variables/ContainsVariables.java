package com.js.interpreter.runtime.variables;

public interface ContainsVariables extends Cloneable {
	public Object get_var(String name);

	public void set_var(String name, Object val);

	public ContainsVariables clone();
}
