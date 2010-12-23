package com.js.interpreter.ast.instructions;

import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.VariableIdentifier;

public class VariableSet implements Executable {
	VariableIdentifier name;

	ReturnsValue value;

	public VariableSet(VariableIdentifier name, ReturnsValue value) {
		this.name = name;
		this.value = value;
	}

	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
		name.set_value(f,main,value.get_value(f, main));
		return ExecutionResult.NONE;
	}

	@Override
	public String toString() {
		return "set [" + name + "] to [" + value + "]\n";
	}
}
