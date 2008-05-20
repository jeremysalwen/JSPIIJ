package pascal_types;

import java.util.HashMap;

import preprocessed.variable_declaration;
import preprocessed.interpreting_objects.variables.contains_variables;

public class custom_type implements contains_variables {
	public HashMap<String, Object> values;

	public custom_type(HashMap<String, Object> values) {
		this.values = (HashMap<String, Object>) values.clone();
	}

	public custom_type(custom_type_declaration c) {
		for (variable_declaration v : c.variable_types) {
			v.initialize(values);
		}
	}

	@Override
	public Object clone() {
		return new custom_type(values);
	}

	@Override
	public Object get_var(String name) {
		return values.get(name);
	}

	@Override
	public void set_var(String name, Object val) {
		values.put(name, val);
	}
}
