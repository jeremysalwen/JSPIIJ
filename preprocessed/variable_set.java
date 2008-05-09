package preprocessed;

import pascalTypes.pascalVar;

public class variable_set<T> implements executable {
	String name;
	returns_value<T> value;

	public variable_set(String name, returns_value<T> value) {
		this.name = name;
		this.value = value;
	}

	public void execute(function_on_stack f) {
		((pascalVar<T>) f.variables.get(name)).set(value.get_value(f).get());
	}

}
