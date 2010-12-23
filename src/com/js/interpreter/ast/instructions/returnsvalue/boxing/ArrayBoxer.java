package com.js.interpreter.ast.instructions.returnsvalue.boxing;

import java.lang.reflect.Array;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.pascaltypes.ArgumentType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class ArrayBoxer implements ReturnsValue {
	ReturnsValue[] values;
	ArgumentType type;

	public ArrayBoxer(ReturnsValue[] value, ArgumentType elementType) {
		this.values = value;
		this.type = elementType;
	}

	@Override
	public RuntimeType get_type(FunctionDeclaration f) {
		throw new RuntimeException(
				"Attempted to get type of varargs boxer. This should not happen as we are only supposed to pass varargs to plugins");
	}

	@Override
	public Object get_value(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
		Object[] result = (Object[]) Array.newInstance(type.getRuntimeClass(),
				values.length);
		for (int i = 0; i < values.length; i++) {
			result[i] = values[i].get_value(f, main);
		}
		return result;
	}

}
