package edu.js.interpreter.preprocessed.interpreting_objects;

import java.lang.reflect.Array;
import java.util.HashMap;

import edu.js.interpreter.preprocessed.function_declaration;
import edu.js.interpreter.preprocessed.variable_declaration;
import edu.js.interpreter.preprocessed.instructions.executable;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.contains_variables;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.subvar_identifier;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.variable_identifier;
import edu.js.interpreter.processing.pascal_program;
import edu.js.interpreter.processing.run_mode;

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
		prototype.instructions.execute(this);
		return local_variables.get("result");
	}

	public Object get_var(String name) {
		if (local_variables.containsKey(name)) {
			return local_variables.get(name);
		} else if (program.main.local_variables.contains(name)) {
			return program.main_running.local_variables.get(name);
		} else if (passed_variables.containsKey(name)) {
			return passed_variables.get(name).get();
		} else {
			System.err.println("Could not find requested variable '" + name
					+ "'");
			return null; // I have to do this
		}
	}

	public void set_var(String name, Object val) {
		if (local_variables.containsKey(name)) {
			local_variables.put(name, val);
		} else if (program.main.local_variables.contains(name)) {
			program.main_running.local_variables.put(name, val);
		} else if (passed_variables.containsKey(name)) {
			passed_variables.get(name).set(val);
		} else {
			System.err.println("Could not find requested variable '" + name
					+ "'");
			System.exit(0);
		}
	}

	public Object get_var(variable_identifier name) {
		zero_length_check(name);
		Object var_holder = get_variable_holder(name);

		if (var_holder instanceof contains_variables) {
			return ((contains_variables) var_holder).get_var(name.get(
					name.size() - 1).string());
		} else {
			int index = ((Number) name.get(name.size() - 1).returnsvalue()
					.get_value(this)).intValue();
			if (var_holder instanceof StringBuilder) {
				return ((StringBuilder) var_holder).charAt(index);
			} else {
				return Array.get(var_holder, index);
			}
		}
	}

	public void set_var(variable_identifier name, Object val) {
		zero_length_check(name);
		if (name.size() == 1) {
			set_var(name.get(0).string(), val);
		}
		Object variable_holder = get_variable_holder(name);
		if (name.get(name.size() - 1).isstring()) {
			((contains_variables) variable_holder).set_var(name.get(
					name.size() - 1).string(), val);
		} else {
			int index = ((Number) name.get(name.size() - 1).returnsvalue()
					.get_value(this)).intValue();
			if (variable_holder instanceof StringBuilder) {
				((StringBuilder) variable_holder).setCharAt(index,
						(Character) val);
			} else {
				Array.set(variable_holder, index, val);
			}
		}
	}

	public Object get_variable_holder(variable_identifier name) {
		Object v = this;
		for (int i = 0; i < name.size() - 1; i++) {
			subvar_identifier index = name.get(i);
			if (index.isreturnsvalue()) {
				int arrayindex = ((Number) index.returnsvalue().get_value(this))
						.intValue();
				if (v instanceof StringBuilder) {
					v = ((StringBuilder) v).charAt(arrayindex);
				} else {
					v = Array.get(v, arrayindex);
				}
			} else {
				v = ((contains_variables) v).get_var(name.get(i).string());
			}
		}
		return v;
	}

	protected void zero_length_check(variable_identifier v) {
		if (v.isEmpty()) {
			System.err.println("Error 0 length variable!");
			System.exit(0);
		}
	}

	public contains_variables clone() {
		return null;
	}

}
