package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class ConstToken extends BasicToken {

	public ConstToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "const";
	}
}
