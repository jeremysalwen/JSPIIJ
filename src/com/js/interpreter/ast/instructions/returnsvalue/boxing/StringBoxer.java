package com.js.interpreter.ast.instructions.returnsvalue.boxing;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringBoxer implements ReturnsValue {

	public StringBoxer(ReturnsValue tobox) {
		this.s = tobox;
	}

	@Override
	public LineInfo getLineNumber() {
		return s.getLineNumber();
	}

	final ReturnsValue s;

	@Override
	public RuntimeType get_type(FunctionDeclaration f) {
		return new RuntimeType(JavaClassBasedType.StringBuilder, false);
	}

	@Override
	public Object get_value(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		return new StringBuilder(s.get_value(f, main).toString());
	}

}
