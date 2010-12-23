package com.js.interpreter.ast.instructions.returnsvalue;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.exceptions.UnconvertableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class BuiltinTypeConversion implements ReturnsValue {
	DeclaredType output;
	ReturnsValue input;

	public BuiltinTypeConversion(LineInfo line, DeclaredType output,
			ReturnsValue input, DeclaredType inputType)
			throws UnconvertableTypeException {
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
	public RuntimeType get_type(FunctionDeclaration f) {
		return new RuntimeType(output, false);
	}

	@Override
	public Object get_value(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
		Object value = input.get_value(f, main);
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
