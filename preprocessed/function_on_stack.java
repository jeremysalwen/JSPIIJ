package preprocessed;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import pascalTypes.custom_type;
import pascalTypes.default_pascal_values;
import processing.pascal_program;

public class function_on_stack {
	HashMap<String, Object> variables = new HashMap<String, Object>();
	function_declaration prototype;
	pascal_program program;

	public function_on_stack(pascal_program program,
			function_declaration declaration) {
		for (variable_declaration v : declaration.local_variables) {
			v.initialize(variables);
		}
		if (prototype.header.return_type != null) {
			variables.put("result", default_pascal_values
					.get_default_value(prototype.header.return_type));
		}
		this.program = program;
		this.prototype = declaration;
	}

	public Object execute(LinkedList<Object> arguments) {
		ListIterator<Object> i = arguments.listIterator();
		for (Object p = null; i.hasNext(); p = i.next()) {
			this.variables.put(this.prototype.header.arguments.get(
					i.previousIndex()).get_name(),
					p instanceof custom_type ? ((custom_type) p).clone() : p);
		}
		for (executable e : prototype.instructions) {
			e.execute(this);
		}
		return this.variables.get("result");
	}
}
