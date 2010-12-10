package com.js.interpreter.runtime.variables;

import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;

public interface SubvarIdentifier {
	public Object get(Object container, VariableContext variableContext,RuntimeExecutable<?> main);

	public void set(Object container, VariableContext context,
			RuntimeExecutable<?> main, Object value);

	public VariableBoxer create_pointer(Object container,
			VariableContext context, RuntimeExecutable<?> main);

	public DeclaredType getType(DeclaredType containerType);

}
