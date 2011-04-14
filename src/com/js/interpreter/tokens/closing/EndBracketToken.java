package com.js.interpreter.tokens.closing;

import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.js.interpreter.exceptions.grouping.GroupingException;
import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException.grouping_exception_types;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.grouping.BracketedToken;
import com.js.interpreter.tokens.grouping.GrouperToken;
import com.js.interpreter.tokens.grouping.ParenthesizedToken;

public class EndBracketToken extends ClosingToken {

	public EndBracketToken(LineInfo line) {
		super(line);
	}

	@Override
	public grouping_exception_types getClosingException(GrouperToken t) {
		return t instanceof BracketedToken ? null
				: grouping_exception_types.MISMATCHED_BRACKETS;
	}

}
