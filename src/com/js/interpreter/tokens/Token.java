package com.js.interpreter.tokens;

import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;

public abstract class Token {
	public LineInfo lineInfo;

	public Token(LineInfo line) {
		this.lineInfo = line;
	}

	public WordToken get_word_value() throws ParsingException {
		throw new ExpectedTokenException("[Identifier]", this);
	}

	/**
	 * Null means not an operator
	 * 
	 * @return
	 */
	public precedence getOperatorPrecedence() {
		return null;
	}

	public enum precedence {
		Dereferencing, Negation, Multiplicative, Additive, Relational, NoPrecedence
	};
}
