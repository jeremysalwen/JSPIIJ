package com.js.interpreter.ast.instructions;

import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;

public class BreakInstruction implements Executable {

	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main) {
		return ExecutionResult.BREAK;
	}

}
