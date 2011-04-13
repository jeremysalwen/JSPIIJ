package com.js.interpreter.exceptions;

import com.js.interpreter.linenumber.LineInfo;

public class GroupingException extends ParsingException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5878580280861132626L;

	public static enum grouping_exception_types {
		MISMATCHED_PARENS("Mismatched parentheses"), MISMATCHED_BRACKETS("Mismatched brackets"),MISMATCHED_BEGIN_END(
				"Mismatched begin-end construct"), UNFINISHED_BEGIN_END(
				"Unfinished begin-end construct"), UNFINISHED_PARENS(
				"You forgot to close your parentheses"),UNFINISHED_BRACKETS(
				"You forgot to close your brackets"), EXTRA_END(
				"You have an extra 'end' in your program"), UNFINISHED_CONSTRUCT(
				"You forgot to complete the structure you started here"), IO_EXCEPTION(
				"IOException occured while reading the input");
		public String message;

		grouping_exception_types(String message) {
			this.message = message;
		}
	};

	public Exception caused;
	grouping_exception_types grouping_exception_type;

	public GroupingException(LineInfo line, grouping_exception_types t) {
		super(line);
		this.grouping_exception_type = t;
	}

	@Override
	public String getMessage() {
		return grouping_exception_type.message
				+ ((caused == null) ? ("") : (": " + caused.getMessage()));
	}

	@Override
	public String toString() {
		return line + ":" + grouping_exception_type.message;
	}
}
