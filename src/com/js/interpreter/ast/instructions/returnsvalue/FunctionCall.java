package com.js.interpreter.ast.instructions.returnsvalue;

import java.util.Arrays;

import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public class FunctionCall implements ReturnsValue, Executable {
	AbstractFunction function;

	ReturnsValue[] arguments;

	public FunctionCall(AbstractFunction function, ReturnsValue[] arguments) {
		this.function = function;
		if (function == null) {
			System.err.println("Warning: Null function call");
		}
		this.arguments = arguments;
	}

	public Object get_value(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
		Object[] values = new Object[arguments.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = arguments[i].get_value(f, main);
			if (function.isByReference(i)) {
				continue;
			}
			if (values[i] instanceof Object[]) {
				values[i] = ((Object[]) values[i]).clone();
			}
			if (values[i] instanceof ContainsVariables) {
				values[i] = ((ContainsVariables) values[i]).clone();
			}
		}
		Object result = function.call(f, main, values);
		return result;
	}

	@Override
	public String toString() {
		return function.name() + "(" + Arrays.toString(arguments) + ')';
	}

	public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
		get_value(f, main);
		return ExecutionResult.NONE;
	}

	public RuntimeType get_type(FunctionDeclaration f) {
		return new RuntimeType(function.return_type(), false);
	}
}
