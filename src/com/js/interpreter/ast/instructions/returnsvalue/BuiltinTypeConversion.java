package com.js.interpreter.ast.instructions.returnsvalue;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.DebuggableExecutable;
import com.js.interpreter.exceptions.UnconvertableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class BuiltinTypeConversion extends DebuggableReturnsValue {
	DeclaredType output;
	ReturnsValue input;
	LineInfo line;

	public BuiltinTypeConversion(LineInfo line, DeclaredType output,
			ReturnsValue input, DeclaredType inputType)
			throws UnconvertableTypeException {
		this.line = line;
		if (output instanceof JavaClassBasedType
				&& inputType instanceof JavaClassBasedType) {
			if (Number.class.isAssignableFrom(output.toclass())
					&& Number.class.isAssignableFrom(inputType.toclass())) {
				this.output = output;
				this.input = input;
				return;
			}
		}
		throw new UnconvertableTypeException(line, inputType, output);
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public RuntimeType get_type(FunctionDeclaration f) {
		return new RuntimeType(output, false);
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object value = input.getValue(f, main);
		if (output == JavaClassBasedType.Integer) {
			return ((Number) value).intValue();
		}
		if (output == JavaClassBasedType.Long) {
			return ((Number) value).longValue();
		}
		if (output == JavaClassBasedType.Double) {
			return ((Number) value).doubleValue();
		}
		// TODO add more conversions
		System.err.println("Not able to automatically convert type " + output);
		return value;
	}

}
