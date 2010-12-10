package com.js.interpreter.tokens.section;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.basic.BasicToken;

public class SectionIdentifierToken extends BasicToken {
	public Sections section;

	public SectionIdentifierToken(LineInfo line, Sections section) {
		super(line);
		this.section = section;
	}

	@Override
	public String toString() {
		return section.toString().toLowerCase();
	}
}
