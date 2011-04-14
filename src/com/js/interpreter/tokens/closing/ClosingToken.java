package com.js.interpreter.tokens.closing;

import com.js.interpreter.exceptions.grouping.GroupingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.grouping.GrouperToken;

public abstract class ClosingToken extends Token {

	public ClosingToken(LineInfo line) {
		super(line);
	}

	/**
	 * Determines if this token can close the give construct, and returns the
	 * correct exception if it can't, and null if it can.
	 * 
	 * @param t
	 * @return
	 */
	public abstract GroupingException getClosingException(GrouperToken t);

}
