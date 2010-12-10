package com.js.interpreter.exceptions;

import com.js.interpreter.linenumber.LineInfo;

public class ExpectedTokenException extends ParsingException {
	public ExpectedTokenException(LineInfo line, String token) {
		super(line, "Expected the following token to appear, it did not: "
				+ token);
	}
}
