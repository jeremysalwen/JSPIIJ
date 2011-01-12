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

public class CharacterBoxer extends DebuggableReturnsValue {
	ReturnsValue c;

	public CharacterBoxer(ReturnsValue c) {
		this.c = c;
	}

	@Override
	public LineInfo getLineNumber() {
		return c.getLineNumber();
	}

	@Override
	public RuntimeType get_type(ExpressionContext f) {
		return new RuntimeType(JavaClassBasedType.StringBuilder, false);
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		return new StringBuilder(c.getValue(f, main).toString());
	}

	@Override
	public Object compileTimeValue() throws ParsingException {
		Object val = c.compileTimeValue();
		if (val != null) {
			return new StringBuilder(val.toString());
		} else {
			return null;
		}
	}

}
