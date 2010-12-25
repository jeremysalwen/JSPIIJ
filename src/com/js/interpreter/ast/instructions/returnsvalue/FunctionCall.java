package com.js.interpreter.ast.instructions.returnsvalue;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.instructions.DebuggableExecutable;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.PluginCallException;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.internal.PluginReflectionException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public class FunctionCall extends DebuggableExecutableReturnsValue {
	AbstractFunction function;

	ReturnsValue[] arguments;
	LineInfo line;

	public FunctionCall(AbstractFunction function, ReturnsValue[] arguments,
			LineInfo line) {
		this.function = function;
		if (function == null) {
			System.err.println("Warning: Null function call");
		}
		this.arguments = arguments;
		this.line = line;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object[] values = new Object[arguments.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = arguments[i].getValue(f, main);
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
		Object result;
		try {
			result = function.call(f, main, values);
		} catch (IllegalArgumentException e) {
			throw new PluginReflectionException(line, e);
		} catch (IllegalAccessException e) {
			throw new PluginReflectionException(line, e);
		} catch (InvocationTargetException e) {
			throw new PluginCallException(line, e.getCause(), function);
		}
		return result;
	}

	@Override
	public String toString() {
		return function.name() + "(" + Arrays.toString(arguments) + ')';
	}

	@Override
	public ExecutionResult executeImpl(VariableContext f,
			RuntimeExecutable<?> main) throws RuntimePascalException {
		getValueImpl(f, main);
		return ExecutionResult.NONE;
	}

	@Override
	public RuntimeType get_type(FunctionDeclaration f) {
		return new RuntimeType(function.return_type(), false);
	}
}
