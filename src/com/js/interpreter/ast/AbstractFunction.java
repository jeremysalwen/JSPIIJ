package com.js.interpreter.ast;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.pascaltypes.ArgumentType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public abstract class AbstractFunction implements NamedEntity {

	public abstract String name();

	public abstract ArgumentType[] argumentTypes();

	public abstract DeclaredType return_type();

	public abstract boolean isByReference(int i);

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(name());
		result.append('(');
		for (ArgumentType c : argumentTypes()) {
			result.append(c);
			result.append(',');
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * This invokes a function call of any type.
	 * 
	 * @param parentcontext
	 *            The program context.
	 * @param arguments
	 * @return The return value of the called function.
	 * @throws RuntimePascalException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public abstract Object call(VariableContext parentcontext,
			RuntimeExecutable<?> main, Object[] arguments)
			throws RuntimePascalException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException;

	/**
	 * 
	 * @param values
	 * @return converted arguments, or null, if they do not fit.
	 * @throws ParsingException
	 */
	public ReturnsValue[] format_args(List<ReturnsValue> values,
			ExpressionContext f) throws ParsingException {
		ArgumentType[] accepted_types = argumentTypes();
		ReturnsValue[] result = new ReturnsValue[accepted_types.length];
		Iterator<ReturnsValue> iterator = values.iterator();
		for (int i = 0; i < accepted_types.length; i++) {
			result[i] = accepted_types[i].convertArgType(iterator, f);
			if (result[i] == null) {/*
									 * This indicates that it cannot fit.
									 */
				return null;
			}
		}
		if (iterator.hasNext()) {
			return null;
		}
		return result;
	}

	public ReturnsValue[] perfectMatch(List<ReturnsValue> args,
			ExpressionContext context) throws ParsingException {
		ArgumentType[] accepted_types = argumentTypes();
		Iterator<ReturnsValue> iterator = args.iterator();
		ReturnsValue[] result = new ReturnsValue[accepted_types.length];
		for (int i = 0; i < accepted_types.length; i++) {
			result[i] = accepted_types[i].perfectFit(iterator, context);
			if (result[i] == null) {
				return null;
			}
		}
		return result;
	}
}
