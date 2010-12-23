package com.js.interpreter.ast.instructions;

import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface Executable {
	/*
	 * This should not modify the state of the object, unless it is a plugin
	 * call.
	 */
	public ExecutionResult execute(VariableContext f,RuntimeExecutable<?> main) throws RuntimePascalException;
}
