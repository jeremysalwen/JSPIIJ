package com.js.interpreter.ast.instructions.returnsvalue.boxing;

import com.js.interpreter.ast.CompileTimeContext;
import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.instructions.returnsvalue.DebuggableReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringBuilderBoxer extends DebuggableReturnsValue {
	ReturnsValue value;

	public StringBuilderBoxer(ReturnsValue value) {
		this.value = value;
	}

	@Override
	public RuntimeType get_type(ExpressionContext f) throws ParsingException {
		return new RuntimeType(JavaClassBasedType.anew(String.class), false);
	}

	@Override
	public LineInfo getLineNumber() {
		return value.getLineNumber();
	}

	@Override
	public Object compileTimeValue(CompileTimeContext context)
			throws ParsingException {
		Object other = value.compileTimeValue(context);
		if (other != null) {
			return ((StringBuilder) other).toString();
		}
		return null;
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object other = value.getValue(f, main);
		return ((StringBuilder) other).toString();
	}

	@Override
	public SetValueExecutable createSetValueInstruction(ReturnsValue r)
			throws UnassignableTypeException {
		throw new UnassignableTypeException(this);
	}
}
