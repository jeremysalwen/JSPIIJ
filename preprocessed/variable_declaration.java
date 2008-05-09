package preprocessed;

import java.util.TreeMap;

import pascalTypes.pascalVar;

public class variable_declaration {
	public String name;
	public Class<?> type;

	public variable_declaration(String name, Class<?> type) {
		this.type = type;
		this.name = name;
	}

	public void initialize(TreeMap<String, pascalVar> variables) {
		variables.put(name, new pascalVar(type));
	}
}
