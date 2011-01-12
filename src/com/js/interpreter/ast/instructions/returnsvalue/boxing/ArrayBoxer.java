package com.js.interpreter.ast.instructions.returnsvalue.boxing;

import java.lang.reflect.Array;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.DebuggableReturnsValue;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.ArgumentType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class ArrayBoxer extends DebuggableReturnsValue {
	ReturnsValue[] values;
	ArgumentType type;
	LineInfo line;

	public ArrayBoxer(ReturnsValue[] value, ArgumentType elementType,
			LineInfo line) {
		this.values = value;
		this.type = elementType;
		this.line = line;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public RuntimeType get_type(ExpressionContext f) {
		throw new RuntimeException(
				"Attempted to get type of varargs boxer. This should not happen as we are only supposed to pass varargs to plugins");
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object[] result = (Object[]) Array.newInstance(type.getRuntimeClass(),
				values.length);
		for (int i = 0; i < values.length; i++) {
			result[i] = values[i].getValue(f, main);
		}
		return result;
	}

	@Override
	public Object compileTimeValue() throws ParsingException {
		Object[] result = (Object[]) Array.newInstance(type.getRuntimeClass(),
				values.length);
		for (int i = 0; i < values.length; i++) {
			Object val = values[i].compileTimeValue();
			if (val == null) {
				return null;
			} else {
				result[i] = val;
			}
		}
		return result;
	}

}
