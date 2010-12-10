package com.js.interpreter.tokens.value;

import com.js.interpreter.linenumber.LineInfo;

public class BooleanToken extends ValueToken {
	boolean b;

	public BooleanToken(LineInfo line, boolean b) {
		super(line);
		this.b = b;
	}

	public Object getValue() {
		return b;
	}
}
