package com.js.interpreter.tokens.closing;

import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.js.interpreter.exceptions.grouping.GroupingException;
import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException.grouping_exception_types;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.grouping.BracketedToken;
import com.js.interpreter.tokens.grouping.GrouperToken;
import com.js.interpreter.tokens.grouping.ParenthesizedToken;

public class EndParenToken extends ClosingToken {

	public EndParenToken(LineInfo line) {
		super(line);
	}

	@Override
	public grouping_exception_types getClosingException(GrouperToken t) {
		return t instanceof ParenthesizedToken ? null
				: grouping_exception_types.MISMATCHED_PARENS;
	}
}
