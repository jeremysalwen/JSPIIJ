package com.js.interpreter.tokens;

import com.js.interpreter.exceptions.GroupingException;
import com.js.interpreter.linenumber.LineInfo;

public class GroupingExceptionToken extends Token {
	public GroupingException exception;

	public GroupingExceptionToken(LineInfo line,
			GroupingException.grouping_exception_types type) {
		super(line);
		this.exception = new GroupingException(line,type);
	}

}
