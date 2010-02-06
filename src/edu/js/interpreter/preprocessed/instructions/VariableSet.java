package edu.js.interpreter.preprocessed.instructions;

import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.VariableIdentifier;

public class VariableSet implements Executable {
	VariableIdentifier name;

	ReturnsValue value;

	public VariableSet(VariableIdentifier name, ReturnsValue value) {
		this.name = name;
		this.value = value;
	}

	public boolean execute(FunctionOnStack f) {
		f.set_var(name, value.get_value(f));
		return false;
	}

	@Override
	public String toString() {
		return "set [" + name + "] to [" + value + "]\n";
	}
}
