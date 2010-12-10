package com.js.interpreter.tokens.section;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.basic.BasicToken;

public class SectionToken extends BasicToken {
	public SectionToken(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "Section";
	}
}
