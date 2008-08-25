package preprocessed.interpreting_objects;

import java.util.HashMap;

import preprocessed.function_declaration;
import preprocessed.variable_declaration;
import preprocessed.instructions.executable;
import preprocessed.interpreting_objects.variables.contains_variables;
import processing.pascal_program;

public class function_on_stack implements contains_variables {
	public HashMap<String, Object> variables = new HashMap<String, Object>();

	public function_declaration prototype;

	public pascal_program program;

	public function_on_stack(pascal_program program,
			function_declaration declaration, Object[] arguments) {
		this.prototype = declaration;
		this.program = program;
		if (prototype.local_variables != null) {
			for (variable_declaration v : prototype.local_variables) {
				v.initialize(variables);
			}
		}
		for (int i = 0; i < arguments.length; i++) {
			variables.put(prototype.argument_names.get(i), arguments[i]);
		}
		if (declaration.return_type != null) {
			try {
				variables.put("result", declaration.return_type.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.program = program;
		this.prototype = declaration;
	}

	public Object execute() {
		for (executable e : prototype.instructions) {
			e.execute(this);
		}
		return get_var("result");
	}

	public Object get_var(String name) {
		return variables.get(name);
	}

	public void set_var(String name, Object val) {
		variables.put(name, val);
	}
}
