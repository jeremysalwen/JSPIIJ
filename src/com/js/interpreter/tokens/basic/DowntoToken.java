package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class DowntoToken extends BasicToken {

	public DowntoToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "downto";
	}
}
