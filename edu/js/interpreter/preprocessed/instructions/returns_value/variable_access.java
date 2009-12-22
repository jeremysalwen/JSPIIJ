package edu.js.interpreter.preprocessed.instructions.returns_value;

import edu.js.interpreter.pascal_types.class_pascal_type;
import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.function_declaration;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.variable_identifier;
import edu.js.interpreter.processing.pascal_program;

public class variable_access implements returns_value {
	variable_identifier variable_name;

	public variable_access(variable_identifier name) {
		this.variable_name = name;
	}

	public Object get_value(function_on_stack f) {
		return f.get_var(variable_name);
	}

	@Override
	public String toString() {
		return "get_variable[" + variable_name + ']';
	}

	public pascal_type get_type(function_declaration f) {
		pascal_type type = f.get_variable_type(variable_name.get(0).string());
		if (type == null) {
			type = f.program.main.get_variable_type(variable_name.get(0).string());
		}
		for (int i = 1; i < variable_name.size(); i++) {
			if (variable_name.get(i).isstring()) {
				try {
					type = class_pascal_type.anew(type.toclass().getField(
							variable_name.get(i).string()).getType());
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			} else {
				type = class_pascal_type
						.anew(type.toclass().getComponentType());
			}
		}
		assert (type != null);
		return type;
	}
}
