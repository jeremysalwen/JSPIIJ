package com.js.interpreter.ast.instructions.returnsvalue.boxing;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.returnsvalue.DebuggableReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringBoxer extends DebuggableReturnsValue {

	public StringBoxer(ReturnsValue tobox) {
		this.s = tobox;
	}

	@Override
	public LineInfo getLineNumber() {
		return s.getLineNumber();
	}

	final ReturnsValue s;

	@Override
	public RuntimeType get_type(ExpressionContext f) {
		return new RuntimeType(JavaClassBasedType.StringBuilder, false);
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		return new StringBuilder(s.getValue(f, main).toString());
	}

	@Override
	public Object compileTimeValue() throws ParsingException {
		Object o = s.compileTimeValue();
		if (o != null) {
			return new StringBuilder(o.toString());
		} else {
			return null;
		}
	}

}
