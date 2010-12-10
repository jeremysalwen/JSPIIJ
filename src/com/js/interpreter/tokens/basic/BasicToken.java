package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.Token;

public abstract class BasicToken extends Token {

	public BasicToken(LineInfo line) {
		super(line);
	}

}
