package com.js.interpreter.tokens.grouping;

import com.js.interpreter.linenumber.LineInfo;

public class TypeToken extends GrouperToken {
	public TypeToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "type";
	}
}
