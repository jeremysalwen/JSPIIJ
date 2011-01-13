package com.js.interpreter.runtime.variables;

import com.js.interpreter.ast.CompileTimeContext;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface SubvarIdentifier {
	public Object get(Object container, VariableContext variableContext,
			RuntimeExecutable<?> main) throws RuntimePascalException;

	public void set(Object container, VariableContext context,
			RuntimeExecutable<?> main, Object value)
			throws RuntimePascalException;

	public VariableBoxer create_pointer(Object container,
			VariableContext context, RuntimeExecutable<?> main)
			throws RuntimePascalException;

	public DeclaredType getType(DeclaredType containerType)
			throws ParsingException;

	public Object compileTimeGet(Object container, CompileTimeContext context)
			throws ParsingException;
}
