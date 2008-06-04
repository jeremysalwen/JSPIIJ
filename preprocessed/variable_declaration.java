package preprocessed;

import java.util.HashMap;

import pascal_types.pascal_type;
import preprocessed.interpreting_objects.variables.contains_variables;

public class variable_declaration {
	private String name;
	private pascal_type type;
	public String get_name() {
		return name;
	}

	public variable_declaration(String name, pascal_type type) {
		this.name = name;
		this.type = type;
	}


	public void initialize(HashMap<String, Object> variable_map) {
		variable_map.put(name, type.get_new());
	}

	public void initialize(contains_variables v) {
		v.set_var(name, type.get_new());
	}
}
