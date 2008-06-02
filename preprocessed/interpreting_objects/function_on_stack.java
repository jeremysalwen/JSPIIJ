package preprocessed.interpreting_objects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import pascal_types.custom_type;
import pascal_types.custom_type_declaration;
import pascal_types.pascal_type_methods;
import preprocessed.function_declaration;
import preprocessed.variable_declaration;
import preprocessed.instructions.executable;
import preprocessed.interpreting_objects.variables.contains_variables;
import processing.pascal_program;

public class function_on_stack implements contains_variables {
	public HashMap<String, Object> variables = new HashMap<String, Object>();
	function_declaration prototype;
	public pascal_program program;

	@SuppressWarnings("unchecked")
	public function_on_stack(pascal_program program,
			function_declaration declaration) {
		this.prototype = declaration;
		this.program = program;
		for (variable_declaration v : prototype.local_variables) {
			v.initialize(variables);
		}
		if (prototype.header.return_type != null) {
			if (prototype.header.return_type instanceof Class) {
				variables
						.put(
								"result",
								pascal_type_methods
										.get_default_value((Class) prototype.header.return_type));
			} else if (prototype.header.return_type instanceof custom_type_declaration) {
				variables
						.put(
								"result",
								((custom_type_declaration) prototype.header.return_type)
										.new_var());
			}
		}
		this.program = program;
		this.prototype = declaration;
	}

	public Object execute(LinkedList<Object> arguments) {
		ListIterator<Object> i = arguments.listIterator();
		for (Object p = null; i.hasNext(); p = i.next()) {
			this.variables.put(prototype.header.arguments
					.get(i.previousIndex()).get_name(),
					p instanceof custom_type ? ((custom_type) p).clone() : p);
		}
		for (executable e : prototype.instructions) {
			e.execute(this);
		}
		return get_var("result");
	}

	@Override
	public Object get_var(String name) {
		return variables.get(name);
	}

	@Override
	public void set_var(String name, Object val) {
		variables.put(name, val);
	}
}
