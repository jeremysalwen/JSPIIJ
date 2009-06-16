package edu.js.interpreter.exceptions;

public class grouping_exception extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5878580280861132626L;

	public static enum grouping_exception_types {
		MISMATCHED_PARENS, MISMATCHED_BEGIN_END, UNFINISHED_BEGIN_END, UNFINISHED_PARENS, EXTRA_END_PARENS, EXTRA_END,
	};

	grouping_exception_types grouping_exception_type;

	public grouping_exception(grouping_exception_types t) {
		this.grouping_exception_type = t;
	}

	public String toString() {
		switch (grouping_exception_type) {
		case EXTRA_END:
			return "Unmatched 'end' token";
		case EXTRA_END_PARENS:
			return "Unmatched end parentheses";
		case MISMATCHED_BEGIN_END:
			return "Mismatched begin-end construct";
		case MISMATCHED_PARENS:
			return "Mismatched parenthesized construct";
		case UNFINISHED_BEGIN_END:
			return "Unfinished begin-end construct";
		case UNFINISHED_PARENS:
			return "Unfinished parenthesized construct";
		default:
			return "Unknown exception type";
		}
	}
}
