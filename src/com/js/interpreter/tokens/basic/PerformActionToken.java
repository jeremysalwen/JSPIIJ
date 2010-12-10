package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class PerformActionToken extends BasicToken {
	public PerformActionToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "PerformAction";
	}
}
