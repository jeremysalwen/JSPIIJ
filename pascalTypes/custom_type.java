package pascalTypes;

import java.util.HashMap;

import preprocessed.variable_declaration;

public class custom_type extends Object {
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
}
