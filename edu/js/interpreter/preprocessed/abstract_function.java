package edu.js.interpreter.preprocessed;

import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;
import edu.js.interpreter.processing.pascal_program;

public abstract class abstract_function {

	public abstract String name();

	public abstract pascal_type[] argument_types();

	public abstract pascal_type return_type();

	public abstract boolean is_varargs(int i);

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(name());
		result.append('(');
		for (pascal_type c : argument_types()) {
			result.append(c);
			result.append(',');
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * This invokes a function call of any type.
	 * 
	 * @param program
	 *            The program context.
	 * @param arguments
	 * @return The return value of the called function.
	 */
	public abstract Object call(pascal_program program, Object[] arguments);

	/**
	 * 
	 * @param values
	 * @return converted arguments, or null, if they do not fit.
	 */
	public returns_value[] format_args(returns_value[] values,
			function_declaration f) {
		pascal_type[] accepted_types = argument_types();
		if (values.length != accepted_types.length) {
			return null;
		}
		returns_value[] result = new returns_value[accepted_types.length];
		for (int i = 0; i < values.length; i++) {
			result[i] = accepted_types[i].convert(values[i], f);
			if (result[i] == null) {/*
									 * This indicates that it cannot fit.
									 */
				return null;
			}
		}
		return result;
	}
}
