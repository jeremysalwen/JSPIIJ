package edu.js.interpreter.preprocessed;

import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.instructions.returns_value.returns_value;
import edu.js.interpreter.processing.pascal_program;

public abstract class abstract_function {

	public abstract String get_name();

	public pascal_type[] argument_types = null;

	public pascal_type return_type = null;

	public abstract boolean is_varargs(int i);

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(get_name());
		result.append('(');
		for (pascal_type c : argument_types) {
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

	public boolean can_accept(returns_value[] values) {
		return false;
	}
}
