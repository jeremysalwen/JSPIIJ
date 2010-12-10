package com.js.interpreter.exceptions;

import com.js.interpreter.linenumber.LineInfo;

public class ExpectedAnotherTokenException extends ParsingException {
	public ExpectedAnotherTokenException(LineInfo line) {
		super(line,
				"Another Token is expected before the end of this construct");
	}
}
