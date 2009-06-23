package edu.js.interpreter.preprocessed;

import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.processing.pascal_program;

public abstract class abstract_function {

	public abstract String get_name();

	public abstract pascal_type[] get_arg_types();

	public abstract pascal_type get_return_type();

	public abstract boolean is_varargs(int i);

	public int hashCode() {
		int hashcode = get_name().hashCode();
		pascal_type[] types = get_arg_types();
		for (int i = 0; i < types.length; i++) {
			hashcode ^= types[i].hashCode();
		}
		return hashcode;
	}

	public boolean equals(Object obj) {
		if (obj instanceof abstract_function) {
			abstract_function other = (abstract_function) obj;
			if (get_name().equals(other.get_name())) {
				pascal_type[] types = get_arg_types();
				pascal_type[] other_types = other.get_arg_types();
				if (types.length == other_types.length) {
					for (int i = 0; i < types.length; i++) {
						if (!types[i].equals(other_types[i])) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(get_name());
		result.append('(');
		for (pascal_type c : get_arg_types()) {
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
}
