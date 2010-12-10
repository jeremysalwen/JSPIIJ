package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class UntilToken extends BasicToken {

	public UntilToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "until";
	}
}
