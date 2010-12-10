package com.js.interpreter.tokens.grouping;

import com.js.interpreter.linenumber.LineInfo;

public class RecordToken extends GrouperToken {
	public RecordToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "record";
	}
}
