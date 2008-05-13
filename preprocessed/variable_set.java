package preprocessed;

import pascalTypes.standard_var;

public class variable_set<T> implements executable {
	String name;
	returns_value<T> value;

	public variable_set(String name, returns_value<T> value) {
		this.name = name;
		this.value = value;
	}

	public void execute(function_on_stack f) {
		((standard_var<T>) f.variables.get(name)).set(value.get_value(f).get());
	}

}
