package com.js.interpreter.tokens;

import com.js.interpreter.linenumber.LineInfo;

public class WordToken extends Token {
	public String name;

	public WordToken(LineInfo line, String s) {
		super(line);
		this.name = s;
	}

	@Override
	public String toString() {
		return name;
	}
}
