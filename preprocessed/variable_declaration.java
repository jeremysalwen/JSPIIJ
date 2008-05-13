package preprocessed;

import java.util.HashMap;

import pascalTypes.standard_var;

public class variable_declaration {
	public String name;
	public Class type;

	public variable_declaration(String name, Class<?> type) {
		this.type = type;
		this.name = name;
	}

	public void initialize(HashMap<String, standard_var> variables) {
		variables.put(name, new standard_var(type));
	}

	@Override
	public int hashCode() {
		return name.hashCode() ^ type.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof variable_declaration) {
			variable_declaration v = (variable_declaration) obj;
			return v.name.equals(name) && v.type == type;
		}
		return false;
	}
}
