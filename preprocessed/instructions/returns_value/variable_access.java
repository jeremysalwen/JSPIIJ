package preprocessed.instructions.returns_value;

import java.util.ListIterator;

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

	public Object get_value(function_on_stack f) { // fix dix
		ListIterator<String> iterator = variable_name.listIterator();
		contains_variables v = f;
		for (int i = 0; i < variable_name.size() - 1; v = (contains_variables) v
				.get_var(iterator.next()))
			;
		return v.get_var(iterator.next());
	}

	@Override
	public String toString() {
		return "get_variable[" + variable_name + ']';
	}

	public Class get_type(pascal_program p, function_declaration f) {
		// TODO
		return null;
	}
}
