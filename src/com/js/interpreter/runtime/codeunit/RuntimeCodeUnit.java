package com.js.interpreter.runtime.codeunit;

import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.codeunit.RunMode;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public abstract class RuntimeCodeUnit<parent extends CodeUnit> extends
		VariableContext {
	
	public volatile RunMode mode;

	public abstract parent getDefinition();

	@Override
	protected Object getLocalVar(String name) throws RuntimePascalException {
		return getDefinition().constants.get(name);
	}

}
