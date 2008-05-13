package preprocessed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.TreeMap;

import pascalTypes.pascal_type;
import pascalTypes.standard_type;
import pascalTypes.standard_var;
import processing.pascal_program;

public class function_on_stack {
	HashMap<String, standard_var> variables = new HashMap<String, standard_var>();
	function_declaration prototype;
	pascal_program program;

	public function_on_stack(pascal_program program,
			function_declaration declaration) {
		for (variable_declaration v : declaration.local_variables) {
			v.initialize(variables);
		}
		if (prototype.header.return_type != null) {
			variables.put("result", new standard_var(
					prototype.header.return_type));
		}
		this.program = program;
		this.prototype = declaration;
	}

	public pascal_type execute(LinkedList<pascal_type> arguments) {
		ListIterator<pascal_type> i = arguments.listIterator();
		for (pascal_type p = null; i.hasNext(); p = i.next()) {
			this.variables.put(this.prototype.header.arguments.get(i
					.previousIndex()).name, new standard_var(p));
		}
		for(executable e:prototype.instructions) {
			e.execute(this);
		}
		return this.variables.get("result");
	}
}
