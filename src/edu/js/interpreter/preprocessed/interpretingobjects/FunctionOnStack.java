package edu.js.interpreter.preprocessed.interpretingobjects;

import java.lang.reflect.Array;
import java.util.HashMap;

import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.VariableDeclaration;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.ContainsVariables;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.SubvarIdentifier;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.VariableIdentifier;
import edu.js.interpreter.processing.PascalProgram;

public class FunctionOnStack implements ContainsVariables {
	public HashMap<String, Object> local_variables = new HashMap<String, Object>();

	public FunctionDeclaration prototype;

	public PascalProgram program;

	HashMap<String, Pointer> reference_variables;

	public FunctionOnStack(PascalProgram program,
			FunctionDeclaration declaration, Object[] arguments) {
		this.prototype = declaration;
		this.program = program;
		if (prototype.local_variables != null) {
			for (VariableDeclaration v : prototype.local_variables) {
				v.initialize(local_variables);
			}
		}
		reference_variables = new HashMap<String, Pointer>();
		for (int i = 0; i < arguments.length; i++) {
			if (prototype.is_varargs(i)) {
				reference_variables.put(prototype.argument_names[i],
						(Pointer) arguments[i]);
			} else {
				local_variables.put(prototype.argument_names[i], arguments[i]);
			}
		}
		if (declaration.return_type != null) {
			new VariableDeclaration("result", prototype.return_type)
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
		} else if (reference_variables.containsKey(name)) {
			return reference_variables.get(name).get();
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
		} else if (reference_variables.containsKey(name)) {
			reference_variables.get(name).set(val);
		} else {
			System.err.println("Could not find requested variable '" + name
					+ "'");
			System.exit(0);
		}
	}

	public Object get_var(VariableIdentifier name) {
		zero_length_check(name);
		Object var_holder = get_variable_holder(name);
		return name.get(name.size() - 1).get(var_holder, this);
	}

	public void set_var(VariableIdentifier name, Object val) {
		zero_length_check(name);
		Object variable_holder = get_variable_holder(name);
		name.get(name.size() - 1).set(variable_holder, this, val);
	}

	public Object get_variable_holder(VariableIdentifier name) {
		Object v = this;
		for (int i = 0; i < name.size() - 1; i++) {
			SubvarIdentifier index = name.get(i);
			v = index.get(v, this);
		}
		return v;
	}

	protected void zero_length_check(VariableIdentifier v) {
		if (v.isEmpty()) {
			System.err.println("Error 0 length variable!");
			System.exit(0);
		}
	}

	@Override
	public ContainsVariables clone() {
		return null;
	}

}
