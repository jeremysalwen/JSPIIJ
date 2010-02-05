package edu.js.interpreter.preprocessed.instructions;

import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;
import edu.js.interpreter.preprocessed.interpreting_objects.variables.variable_identifier;

public class variable_set implements executable {
	variable_identifier name;

	returns_value value;

	public variable_set(variable_identifier name, returns_value value) {
		this.name = name;
		this.value = value;
	}

	public boolean execute(function_on_stack f) {
		f.set_var(name, value.get_value(f));
		return false;
	}

	@Override
	public String toString() {
		return "set [" + name + "] to [" + value + "]\n";
	}
}
