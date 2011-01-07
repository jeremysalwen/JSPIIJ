package com.js.interpreter.exceptions;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.Token;

public class ExpectedTokenException extends ParsingException {
	public ExpectedTokenException(String token, Token instead) {
		super(instead.lineInfo,
				"Expected the following token to appear, it did not: " + token
						+ ".  Instead, there was the following token: "
						+ instead);
	}
}
