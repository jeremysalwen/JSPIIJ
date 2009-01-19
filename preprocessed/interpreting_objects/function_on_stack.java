package preprocessed.interpreting_objects;

import java.util.HashMap;

import preprocessed.function_declaration;
import preprocessed.variable_declaration;
import preprocessed.instructions.executable;
import preprocessed.interpreting_objects.variables.contains_variables;
import preprocessed.interpreting_objects.variables.variable_identifier;
import processing.pascal_program;

public class function_on_stack implements contains_variables {
	public HashMap<String, Object> local_variables = new HashMap<String, Object>();

	public function_declaration prototype;

	public pascal_program program;

	HashMap<String, pointer> passed_variables;

	public function_on_stack(pascal_program program,
			function_declaration declaration, Object[] arguments) {
		this.prototype = declaration;
		this.program = program;
		if (prototype.local_variables != null) {
			for (variable_declaration v : prototype.local_variables) {
				v.initialize(local_variables);
			}
		}
		passed_variables = new HashMap<String, pointer>();
		for (int i = 0; i < arguments.length; i++) {
			if (arguments[i] instanceof pointer) {
				passed_variables.put(prototype.argument_names.get(i),
						(pointer) arguments[i]);
			} else {
				local_variables.put(prototype.argument_names.get(i),
						arguments[i]);
			}
		}
		if (declaration.return_type != null) {
			new variable_declaration("result", prototype.return_type)
					.initialize(local_variables);
		}
		this.program = program;
		this.prototype = declaration;
	}

	public Object execute() {
		for (executable e : prototype.instructions) {
			e.execute(this);
		}
		return local_variables.get("result");
	}

	public Object get_var(String name) {
		if (local_variables.containsKey(name)) {
			return local_variables.get(name);
		} else if (program.global_variables.containsKey(name)) {
			return program.global_variables.get(name);
		} else if (passed_variables.containsKey(name)) {
			return passed_variables.get(name).get();
		} else {
			System.err.println("Could not find requested variable!");
			System.exit(0);
			return null; // I have to do this
		}
	}

	public void set_var(String name, Object val) {
		if (local_variables.containsKey(name)) {
			local_variables.put(name, val);
		} else if (program.global_variables.containsKey(name)) {
			program.global_variables.put(name, val);
		} else if (passed_variables.containsKey(name)) {
			passed_variables.get(name).set(val);
		} else {
			System.err.println("Could not find requested variable!");
			System.exit(0);
		}
	}

	public Object get_var(variable_identifier name) {
		zero_length_check(name);
		return get_variable_holder(name).get_var(name.get(name.size() - 1));// not
		// recursion
	}

	public void set_var(variable_identifier name, Object val) {
		zero_length_check(name);
		if (name.size() == 1) {
			set_var(name.get(0), val);
		}
		get_variable_holder(name).set_var(name.get(name.size() - 1), val);
	}

	public contains_variables get_variable_holder(variable_identifier name) {
		contains_variables v = this;
		for (int i = 0; i < name.size() - 1; i++) {
			v = (contains_variables) v.get_var(name.get(i));
		}
		return v;
	}

	protected void zero_length_check(variable_identifier v) {
		if (v.isEmpty()) {
			System.err.println("Error 0 length variable!");
			System.exit(0);
		}
	}

}
