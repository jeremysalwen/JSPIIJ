package com.js.interpreter.tokens.grouping;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.Token;

public class BeginEndToken extends GrouperToken {

	public BeginEndToken(LineInfo line) {
		super(line);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3783725988847512384L;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("begin ");
		if (next != null) {
			builder.append(next);
		}
		for (Token t : this.queue) {
			builder.append(t).append(' ');
		}
		builder.append("end ");
		return builder.toString();
	}

	@Override
	protected String getClosingText() {
		return "end";
	}
}
