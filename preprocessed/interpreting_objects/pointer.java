package preprocessed.interpreting_objects;

import java.lang.reflect.Array;

import preprocessed.interpreting_objects.variables.contains_variables;

public class pointer {
	public pointer(String name, Object v, boolean isarray) {
		this.v = v;
		this.variable_name = name;
		this.is_array = isarray;
	}

	Object v; // array or contain_variables

	String variable_name;

	boolean is_array;

	public void set(Object value) {
		if (!is_array) {
			((contains_variables) v).set_var(variable_name, value);
		} else {
			Array.set(v, Integer.valueOf(variable_name), value);
		}
	}

	public Object get() {
		if (!is_array) {
			return ((contains_variables) v).get_var(variable_name);
		} else {
			return Array.get(v, Integer.valueOf(variable_name));
		}
	}
}
