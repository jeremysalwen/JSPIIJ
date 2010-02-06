package edu.js.interpreter.exceptions;

public class GroupingException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5878580280861132626L;

	public static enum grouping_exception_types {
		MISMATCHED_PARENS("Mismatched parenthesis"), MISMATCHED_BEGIN_END(
				"Mismatched begin-end construct"), UNFINISHED_BEGIN_END(
				"Unfinished begin-end construct"), UNFINISHED_PARENS(
				"You forgot to close your parentheses"), EXTRA_END_PARENS(
				"You have an extra closing parenthesis"), EXTRA_END(
				"You have an extra 'end' in your program"), ;
		public String message;

		grouping_exception_types(String message) {
			this.message = message;
		}
	};

	grouping_exception_types grouping_exception_type;

	public GroupingException(grouping_exception_types t) {
		this.grouping_exception_type = t;
	}

	@Override
	public String toString() {
		return grouping_exception_type.message;
	}
}
