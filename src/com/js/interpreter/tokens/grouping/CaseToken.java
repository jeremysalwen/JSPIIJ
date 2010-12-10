package com.js.interpreter.tokens.grouping;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.Token;

public class CaseToken extends GrouperToken {

	public CaseToken(LineInfo line) {
		super(line);
	}
	@Override
	public String toString() {
		StringBuilder tmp = new StringBuilder("case ");
		if (next != null) {
			tmp.append(next);
		}
		for (Token t : this.queue) {
			tmp.append(t).append(' ');
		}
		tmp.append("end");
		return tmp.toString();
	}
}
