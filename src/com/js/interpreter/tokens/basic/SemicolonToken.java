package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class SemicolonToken extends BasicToken {
	public SemicolonToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return ";";
	}
}
