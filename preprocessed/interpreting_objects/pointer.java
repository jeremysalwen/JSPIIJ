package preprocessed.interpreting_objects;

import preprocessed.interpreting_objects.variables.contains_variables;

public class pointer {
	public pointer(String name, contains_variables v) {
		this.v=v;
		this.variable_name=name;
	}
	contains_variables v;

	String variable_name;

	public void set(Object value) {
		v.set_var(variable_name, value);
	}
	public Object get() {
		return v.get_var(variable_name);
	}
}
