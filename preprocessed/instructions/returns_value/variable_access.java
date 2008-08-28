package preprocessed.instructions.returns_value;

import preprocessed.function_declaration;
import preprocessed.interpreting_objects.function_on_stack;
import preprocessed.interpreting_objects.variables.contains_variables;
import preprocessed.interpreting_objects.variables.variable_identifier;
import processing.pascal_program;

public class variable_access implements returns_value {
	variable_identifier variable_name;

	public variable_access(variable_identifier name) {
		this.variable_name = name;
	}

	public Object get_value(function_on_stack f) {
		Object v = f;
		for (String s:variable_name) {
			v=((contains_variables) v).get_var(s);
		}
		return v;
	}

	@Override
	public String toString() {
		return "get_variable[" + variable_name + ']';
	}

	public Class get_type(pascal_program p, function_declaration f) {
		Class type=f.get_variable_type(variable_name.get(0));
		for(int i=1; i<variable_name.size(); i++) {
			try {
				type=type.getField(variable_name.get(i)).getType();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return type;
	}
}
