package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class PeriodToken extends BasicToken {
	public PeriodToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return ".";
	}
}
