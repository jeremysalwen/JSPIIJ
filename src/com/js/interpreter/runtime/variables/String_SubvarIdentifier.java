package com.js.interpreter.runtime.variables;

import com.js.interpreter.pascaltypes.CustomType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.runtime.ContainsVariablesPointer;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class String_SubvarIdentifier implements SubvarIdentifier {
	String s;

	public String_SubvarIdentifier(String s) {
		this.s = s;
	}

	@Override
	public String toString() {
		return s;
	}

	@Override
	public Object get(Object container, VariableContext context, RuntimeExecutable<?> main) throws RuntimePascalException {
		return ((ContainsVariables) container).get_var(s);
	}

	@Override
	public VariableBoxer create_pointer(Object container,
			VariableContext context, RuntimeExecutable<?> main) {
		return new ContainsVariablesPointer((ContainsVariables) container, s);
	}

	@Override
	public DeclaredType getType(DeclaredType containerType) {
		if (!(containerType instanceof CustomType)) {
			System.err
					.println("Error! Tried to find subitem's class of container object with a string index, when that's not applicable");
		}
		return ((CustomType) containerType).getMemberType(this.s);
	}


	@Override
	public void set(Object container, VariableContext context,
			RuntimeExecutable<?> main, Object value) {
		((ContainsVariables) container).set_var(this.s, value);
		
	}

}
