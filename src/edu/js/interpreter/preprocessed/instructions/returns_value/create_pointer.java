package edu.js.interpreter.preprocessed.instructions.returns_value;

import java.util.List;

import edu.js.interpreter.pascal_types.custom_type_declaration;
import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.function_declaration;
import edu.js.interpreter.preprocessed.variable_declaration;
import edu.js.interpreter.preprocessed.interpreting_objects.arraypointer;
import edu.js.interpreter.preprocessed.interpreting_objects.containsvariablespointer;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.contains_variables;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.subvar_identifier;

public class create_pointer implements returns_value {
	variable_access container;

	subvar_identifier index;

	public create_pointer(variable_access container, subvar_identifier index) {
		this.container = container;
		this.index = index;
	}

	public pascal_type get_type(function_declaration f) {
		pascal_type container_type = container.get_type(f);
		if (container_type.isarray()) {
			return container_type.get_type_array().element_type;
		} else {
			custom_type_declaration custom_type = ((custom_type_declaration) container_type);
			List<variable_declaration> subtypes = custom_type.variable_types;
			for (variable_declaration v : subtypes) {
				if (v.name.equals(index.string())) {
					return v.type;
				}
			}
			System.err.println("Error, could not find subvar type "
					+ index.string());
			return null;
		}
	}

	public Object get_value(function_on_stack f) {
		Object value = container.get_value(f);
		if (index.isreturnsvalue()) {
			Integer ind = (Integer) index.returnsvalue().get_value(f);
			assert (value.getClass().isArray());
			return new arraypointer(value, ind);
		} else {
			return new containsvariablespointer((contains_variables) value,
					index.string());
		}
	}
}
