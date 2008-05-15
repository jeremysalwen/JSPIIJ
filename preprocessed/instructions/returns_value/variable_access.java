package preprocessed.instructions.returns_value;

import preprocessed.interpreting_objects.function_on_stack;


public class variable_access extends returns_value {
	String variable_name;

	public variable_access(String name) {
		this.variable_name = name;
	}

	@Override
	public Object get_value(function_on_stack f) {
		return f.variables.get(variable_name);
	}
}
