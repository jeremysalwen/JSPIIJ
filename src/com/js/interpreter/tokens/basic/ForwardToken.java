package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class ForwardToken extends BasicToken {

	public ForwardToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "forward";
	}
}
