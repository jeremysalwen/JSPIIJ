package edu.js.interpreter.pascal_types;

import serp.bytecode.Code;

public abstract class pascal_type {
	public abstract Object initialize();

	public abstract boolean isarray();

	public abstract Class toclass();

	public array_type get_type_array() {
		return ((array_type) this);
	}

	public abstract void get_default_value_on_stack(Code code);
}
