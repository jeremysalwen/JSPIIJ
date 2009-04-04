package preprocessed;

import java.util.Map;

import pascal_types.pascal_type;
import serp.bytecode.BCClass;
import serp.bytecode.Code;

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

	public void add_to_constructor(Code constructor_code) {
		constructor_code.aload().setThis();
		type.get_default_value_on_stack(constructor_code);
		constructor_code.putfield().setField(name,
				constructor_code.getMethod().getDeclarer());
	}
}
