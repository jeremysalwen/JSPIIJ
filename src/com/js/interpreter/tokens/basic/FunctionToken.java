package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class FunctionToken extends BasicToken {
	public FunctionToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "function";
	}
}
