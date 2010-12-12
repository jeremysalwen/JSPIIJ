package com.js.interpreter.ast.instructions.returnsvalue.boxing;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;

public class StringBoxer implements ReturnsValue {
	public StringBoxer(ReturnsValue tobox) {
		this.s = tobox;
	}

	final ReturnsValue s;

	@Override
	public RuntimeType get_type(FunctionDeclaration f) {
		return new RuntimeType(JavaClassBasedType.StringBuilder, false);
	}

	@Override
	public Object get_value(VariableContext f, RuntimeExecutable<?> main) {
		return new StringBuilder(s.get_value(f, main).toString());
	}

}