package com.js.interpreter.exceptions;

import com.js.interpreter.tokens.Token;

public class UnrecognizedTokenException extends ParsingException {
	public UnrecognizedTokenException(Token t) {
		super(t.lineInfo, "The following token doesn't belong here: " + t);

	}
}
