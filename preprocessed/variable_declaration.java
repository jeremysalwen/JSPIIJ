package preprocessed;

import java.util.TreeMap;

import pascalTypes.standard_var;

public class variable_declaration {
	public String name;
	public Class<?> type;

	public variable_declaration(String name, Class<?> type) {
		this.type = type;
		this.name = name;
	}

	public void initialize(TreeMap<String, standard_var> variables) {
		variables.put(name, new standard_var(type));
	}
}
