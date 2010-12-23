package com.js.interpreter.ast.instructions;

import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class BreakInstruction implements Executable {

	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
		return ExecutionResult.BREAK;
	}

}
