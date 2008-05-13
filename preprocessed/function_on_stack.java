package preprocessed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import pascalTypes.standard_type;
import pascalTypes.standard_var;
import processing.pascal_program;

public class function_on_stack<RETURN_TYPE> {
	TreeMap<String, standard_var> variables = new TreeMap<String, standard_var>(
			String.CASE_INSENSITIVE_ORDER);
	function_declaration prototype;
	pascal_program program;

	public function_on_stack(pascal_program program,
			function_declaration declaration) {
		for (variable_declaration v : declaration.local_variables) {
			v.initialize(variables);
		}
		if (prototype.return_type != null) {
			variables.put("result", new standard_var<RETURN_TYPE>(
					(Class<RETURN_TYPE>) prototype.return_type));
		}
		this.program = program;
		this.prototype = declaration;
	}

	public standard_type<RETURN_TYPE> execute(ArrayList<standard_type> arguments) {
		for (int i = 0; i < arguments.size(); i++) {
			standard_type p = arguments.get(i);
			this.variables.put(this.prototype.arguments.get(i).name,
					new standard_var(arguments.get(i)));
		}
		prototype.instructions.execute(this);
		return this.variables.get("result");
	}
}
