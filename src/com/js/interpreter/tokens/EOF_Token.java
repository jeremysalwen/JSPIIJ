package com.js.interpreter.tokens;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.basic.BasicToken;

public class EOF_Token extends BasicToken {
	public EOF_Token(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "EOF";
	}

}
