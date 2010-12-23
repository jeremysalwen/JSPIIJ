package com.js.interpreter.ast.instructions.returnsvalue.boxing;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringBuilderBoxer implements ReturnsValue {
	public StringBuilderBoxer(ReturnsValue s) {
		this.s = s;
	}

	final ReturnsValue s;

	@Override
	public LineInfo getLineNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RuntimeType get_type(FunctionDeclaration f) {
		return new RuntimeType(JavaClassBasedType.anew(String.class), false);
	}

	@Override
	public Object get_value(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		return s.get_value(f, main).toString();
	}

}
