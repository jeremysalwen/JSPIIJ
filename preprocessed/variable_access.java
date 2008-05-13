package preprocessed;

import pascalTypes.pascal_type;

public class variable_access extends returns_value {
	String variable_name;

	public variable_access(String name) {
		this.variable_name = name;
	}

	@Override
	public pascal_type get_value(function_on_stack f) {
		return f.variables.get(variable_name);
	}
}
