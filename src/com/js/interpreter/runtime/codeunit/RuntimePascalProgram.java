package com.js.interpreter.runtime.codeunit;

import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.ast.codeunit.RunMode;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class RuntimePascalProgram extends RuntimeExecutable<PascalProgram> {

	FunctionOnStack main;

	public RuntimePascalProgram(PascalProgram p) {
		super(p);
	}

	@Override
	public void run() throws RuntimePascalException {
		this.mode = RunMode.running;
		main = new FunctionOnStack(this, this, definition.main, new Object[0]);
		main.execute();
	}

	@Override
	public VariableContext getParentContext() {
		return null;
	}
}
