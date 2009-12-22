package edu.js.interpreter.pascal_types;

import javax.naming.OperationNotSupportedException;

import edu.js.interpreter.preprocessed.interpreting_objects.pointer;

import serp.bytecode.Code;

public class pointer_type extends pascal_type {
	pascal_type child_type;

	public pointer_type(pascal_type child) {
		this.child_type = child;
	}

	@Override
	public void get_default_value_on_stack(Code code) {
		System.err
				.println("Tried to generate bytecode to initialize variable of pointer type");
		System.exit(1);
	}

	@Override
	public Object initialize() {
		System.err.println("Tried to initialize variable of pointer type");
		System.exit(1);
		return null;
	}

	@Override
	public boolean isarray() {
		return false;
	}

	@Override
	public Class toclass() {
		return pointer.class;
	}

}
