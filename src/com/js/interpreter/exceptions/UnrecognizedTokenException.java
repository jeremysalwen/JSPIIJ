package com.js.interpreter.exceptions;

import com.js.interpreter.tokens.Token;

public class UnrecognizedTokenException extends ParsingException {
	public UnrecognizedTokenException(Token t) {
		super(t.lineInfo, "Unable to handle token: " + t);
	}
}
