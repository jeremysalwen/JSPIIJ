package pascal_types;

import java.util.ArrayList;
import java.util.List;

import preprocessed.variable_declaration;

public class custom_type_declaration {
	/**
	 * This class represents a declaration of a new type in pascal.
	 */

	/**
	 * This is a list of the defined variables in the custom type.
	 */
	public List<variable_declaration> variable_types;

	public custom_type_declaration() {
		variable_types = new ArrayList<variable_declaration>();
	}

	/**
	 * Adds another sub-variable to this user defined type.
	 * 
	 * @param v
	 *            The name and type of the variable to add.
	 */
	public void add_variable_declaration(variable_declaration v) {
		variable_types.add(v);
	}

}
