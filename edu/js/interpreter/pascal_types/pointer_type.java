package edu.js.interpreter.pascal_types;

import serp.bytecode.Code;
import edu.js.interpreter.preprocessed.function_declaration;
import edu.js.interpreter.preprocessed.instructions.returns_value.create_pointer;
import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;
import edu.js.interpreter.preprocessed.instructions.returns_value.variable_access;
import edu.js.interpreter.preprocessed.interpreting_objects.pointer;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.variable_identifier;

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

	@Override
	public returns_value convert(returns_value value, function_declaration f) {
		if (value instanceof variable_access) {
			variable_access access = (variable_access) value;
			variable_identifier identifier = new variable_identifier();
			for (int i = 0; i < access.variable_name.size() - 1; i++) {
				identifier.add(access.variable_name.get(i));
			}
			variable_access new_access = new variable_access(identifier);
			return new create_pointer(new_access, access.variable_name
					.get(access.variable_name.size() - 1));
		}
		return null;
	}

}
