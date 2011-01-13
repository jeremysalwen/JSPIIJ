package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class TypeToken extends BasicToken {
	public TypeToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "type";
	}

}
