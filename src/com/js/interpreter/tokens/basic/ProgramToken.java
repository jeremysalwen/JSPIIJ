package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class ProgramToken extends BasicToken {

	public ProgramToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "program";
	}
}
