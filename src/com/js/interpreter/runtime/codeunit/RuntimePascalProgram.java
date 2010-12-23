package com.js.interpreter.runtime.codeunit;

import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.ast.codeunit.RunMode;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class RuntimePascalProgram extends RuntimeExecutable<PascalProgram> {

	PascalProgram program;

	FunctionOnStack main;

	public RuntimePascalProgram(PascalProgram p) {
		this.program = p;
	}

	@Override
	public PascalProgram getDefinition() {
		return program;
	}

	@Override
	public Object getLocalVar(String name) throws RuntimePascalException {
		Object result = super.getLocalVar(name);
		if (result != null) {
			return result;
		}
		return main.getLocalVar(name);
	}

	@Override
	public boolean setLocalVar(String name, Object value) {
		return main.setLocalVar(name, value);
	}

	@Override
	public void run() throws RuntimePascalException{
		this.mode = RunMode.running;
		main = new FunctionOnStack(this, this, program.main, new Object[0]);
		main.execute();
	}

	@Override
	public VariableContext getParentContext() {
		return null;
	}
}
