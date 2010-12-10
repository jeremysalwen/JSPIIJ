package com.js.interpreter.tokens;

import com.js.interpreter.linenumber.LineInfo;

public abstract class Token {
	public LineInfo lineInfo;

	public Token(LineInfo line) {
		this.lineInfo = line;
	}
}
