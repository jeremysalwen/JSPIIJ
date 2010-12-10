package com.js.interpreter.tokens.value;

import com.js.interpreter.linenumber.LineInfo;

public class StringToken extends ValueToken {
	public String value;

	public StringToken(LineInfo line, String s) {
		super(line);
		this.value = s;
	}

	@Override
	public String toString() {
		return new StringBuilder().append('"').append(value).append('"')
				.toString();
	}

	@Override
	public Object getValue() {
		return new StringBuilder(value);
	}
}
