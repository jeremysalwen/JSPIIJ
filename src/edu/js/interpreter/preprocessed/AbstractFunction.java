package edu.js.interpreter.preprocessed;

import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.processing.PascalProgram;

public abstract class AbstractFunction {

	public abstract String name();

	public abstract PascalType[] argument_types();

	public abstract PascalType return_type();

	public abstract boolean is_varargs(int i);

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(name());
		result.append('(');
		for (PascalType c : argument_types()) {
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
	public abstract Object call(PascalProgram program, Object[] arguments);

	/**
	 * 
	 * @param values
	 * @return converted arguments, or null, if they do not fit.
	 */
	public ReturnsValue[] format_args(ReturnsValue[] values,
			FunctionDeclaration f) {
		PascalType[] accepted_types = argument_types();
		if (values.length != accepted_types.length) {
			return null;
		}
		ReturnsValue[] result = new ReturnsValue[accepted_types.length];
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
