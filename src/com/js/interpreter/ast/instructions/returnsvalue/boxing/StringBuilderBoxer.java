package com.js.interpreter.ast.instructions.returnsvalue.boxing;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;

public class StringBuilderBoxer implements ReturnsValue {
	public StringBuilderBoxer(ReturnsValue s) {
		this.s = s;
	}

	final ReturnsValue s;

	@Override
	public RuntimeType get_type(FunctionDeclaration f) {
		return new RuntimeType(JavaClassBasedType.anew(String.class), false);
	}

	@Override
	public Object get_value(VariableContext f, RuntimeExecutable<?> main) {
		return s.get_value(f, main).toString();
	}

}
