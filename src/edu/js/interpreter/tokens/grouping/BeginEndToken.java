package edu.js.interpreter.tokens.grouping;

import edu.js.interpreter.tokens.Token;

public class BeginEndToken extends GrouperToken {

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
}
