package com.js.interpreter.ast.instructions.case_statement;

import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface CaseCondition {
	public boolean fits(RuntimeExecutable<?> main,VariableContext f, Object value) throws RuntimePascalException;
}
