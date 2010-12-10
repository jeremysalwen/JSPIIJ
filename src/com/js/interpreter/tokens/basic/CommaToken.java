package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class CommaToken extends BasicToken {
	public CommaToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return ",";
	}
}
