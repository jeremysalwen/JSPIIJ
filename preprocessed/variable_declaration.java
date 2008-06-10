package preprocessed;

import java.util.HashMap;

import pascal_types.pascal_type;
import preprocessed.interpreting_objects.variables.contains_variables;
import serp.bytecode.BCClass;
import serp.bytecode.BCField;

public class variable_declaration {
	private String name;
	private Class type;

	public String get_name() {
		return name;
	}

	public variable_declaration(String name, Class type) {
		this.name = name;
		this.type = type;
	}

	public void add_declaration(BCClass c) {
		c.declareField(name, type);
	}
}
