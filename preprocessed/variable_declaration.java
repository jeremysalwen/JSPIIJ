package preprocessed;

import java.util.HashMap;

import pascal_types.custom_type_declaration;
import pascal_types.pascal_type_methods;

public class variable_declaration {
	private String name;
	private Object type;
	private boolean is_custom_declaration;

	public String get_name() {
		return name;
	}

	public variable_declaration(String name, Object type) {
		this.name = name;
		this.type = type;
		is_custom_declaration = false;
	}

	public variable_declaration(String name, custom_type_declaration c) {
		this.name = name;
		this.type = c;
		is_custom_declaration = true;
	}

	public void initialize(HashMap<String, Object> variable_map) {
		if (!is_custom_declaration) {
			variable_map.put(name, pascal_type_methods
					.get_default_value((Class) type));
		} else {
			variable_map.put(name, ((custom_type_declaration) type).new_var());
		}
	}
}
