package com.js.interpreter.ast.instructions.returnsvalue;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;

public class ConstantAccess implements ReturnsValue {
	final Object constant_value;
	final LineInfo line;

	public ConstantAccess(Object o, LineInfo line) {
		this.constant_value = o;
		this.line = line;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	public Object get_value(VariableContext f, RuntimeExecutable<?> main) {
		return constant_value;
	}

	@Override
	public String toString() {
		return constant_value.toString();
	}

	public RuntimeType get_type(FunctionDeclaration f) {
		return new RuntimeType(JavaClassBasedType.anew(constant_value
				.getClass()), false);
	}

}
