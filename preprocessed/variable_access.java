package preprocessed;

import pascalTypes.pascalType;

public class variable_access<T> extends returns_value<T> {
	String variable_name;

	public variable_access(String name) {
		this.variable_name = name;
	}

	@Override
	public pascalType<T> get_value(function_on_stack f) {
		return (pascalType<T>) f.variables.get(variable_name);  //TODO a bug in java i should not have to cast.
	}
}
