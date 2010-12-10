package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class ArrayToken extends BasicToken {

	public ArrayToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "array";
	}
}
