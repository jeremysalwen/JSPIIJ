package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class AssignmentToken extends BasicToken {
	public AssignmentToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return ":=";
	}
}
