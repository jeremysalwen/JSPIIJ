package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class WhileToken extends BasicToken {
	public WhileToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "while";
	}
}
