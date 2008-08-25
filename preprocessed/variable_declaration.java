package preprocessed;

import java.util.Map;

import serp.bytecode.BCClass;

public class variable_declaration {
	public String name;
	public Class type;

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
	public void initialize(Map<String, Object> map) {
		try {
			map.put(name, type.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
