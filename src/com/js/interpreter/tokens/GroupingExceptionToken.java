package com.js.interpreter.tokens;

import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException.grouping_exception_types;
import com.js.interpreter.exceptions.grouping.GroupingException;
import com.js.interpreter.linenumber.LineInfo;

public class GroupingExceptionToken extends Token {
	public GroupingException exception;

	public GroupingExceptionToken(GroupingException g) {
		super(g.line);
		this.exception = g;
	}

	public GroupingExceptionToken(LineInfo line, grouping_exception_types type) {
		super(line);
		this.exception = new EnumeratedGroupingException(line, type);
	}

	@Override
	public String toString() {
		return exception.toString();
	}
}
