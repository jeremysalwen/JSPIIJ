package preprocessed;

import java.util.Map;

import pascal_types.pascal_type;
import serp.bytecode.BCClass;

public class variable_declaration {
	public String name;

	public pascal_type type;

	public String get_name() {
		return name;
	}

	public variable_declaration(String name, pascal_type type) {
		this.name = name;
		this.type = type;
	}

	public void add_declaration(BCClass c) {
		// TODO
	}

	public void initialize(Map<String, Object> map) {
		map.put(name, type.initialize());
	}

}
