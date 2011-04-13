package com.js.interpreter.tokens.closing;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.Token;

public class EndToken extends Token {

	public EndToken(LineInfo line) {
		super(line);
	}

}
