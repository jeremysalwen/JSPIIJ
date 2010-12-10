package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class DoToken extends BasicToken {
	public DoToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "do";
	}
}
