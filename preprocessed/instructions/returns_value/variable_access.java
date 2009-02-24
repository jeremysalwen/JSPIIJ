package preprocessed.instructions.returns_value;

import pascal_types.class_pascal_type;
import pascal_types.pascal_type;
import preprocessed.function_declaration;
import preprocessed.interpreting_objects.function_on_stack;
import preprocessed.interpreting_objects.variables.variable_identifier;
import processing.pascal_program;

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

	public pascal_type get_type(pascal_program p, function_declaration f) {
		pascal_type type = f.get_variable_type(variable_name.get(0).string());
		for (int i = 1; i < variable_name.size(); i++) {
			if (variable_name.get(i).isstring()) {
				try {
					type = new class_pascal_type(type.toclass().getField(
							variable_name.get(i).string()).getType());
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			} else {
				type = new class_pascal_type(type.getClass().getComponentType());
			}
		}
		return type;
	}
}
