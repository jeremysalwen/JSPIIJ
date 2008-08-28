package pascal_types;

import java.util.ArrayList;
import java.util.List;

import preprocessed.variable_declaration;

public class custom_type_declaration {
	public List<variable_declaration> variable_types;

	public custom_type_declaration() {
		variable_types = new ArrayList<variable_declaration>();
	}

	public void add_variable_declaration(variable_declaration v) {
		variable_types.add(v);
	}

}
