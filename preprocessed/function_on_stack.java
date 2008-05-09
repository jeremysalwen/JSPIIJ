package preprocessed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import pascalTypes.pascalType;
import pascalTypes.pascalVar;
import processing.pascal_program;

public class function_on_stack<RETURN_TYPE> {
	TreeMap<String, pascalVar> variables = new TreeMap<String, pascalVar>(
			String.CASE_INSENSITIVE_ORDER);
	function_declaration prototype;
	pascal_program program;

	public function_on_stack(pascal_program program,
			function_declaration declaration) {
		for (variable_declaration v : declaration.local_variables) {
			v.initialize(variables);
		}
		if (prototype.return_type != null) {
			variables.put("result", new pascalVar<RETURN_TYPE>(
					(Class<RETURN_TYPE>) prototype.return_type));
		}
		this.program = program;
		this.prototype = declaration;
	}

	public pascalType<RETURN_TYPE> execute(ArrayList<pascalType> arguments) {
		for (int i = 0; i < arguments.size(); i++) {
			pascalType p = arguments.get(i);
			this.variables.put(this.prototype.arguments.get(i).name,
					new pascalVar(arguments.get(i)));
		}
		prototype.instructions.execute(this);
		return this.variables.get("result");
	}
}
