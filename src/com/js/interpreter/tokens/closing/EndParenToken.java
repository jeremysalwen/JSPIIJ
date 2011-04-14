package com.js.interpreter.tokens.closing;

import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException.grouping_exception_types;
import com.js.interpreter.exceptions.grouping.GroupingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.grouping.GrouperToken;
import com.js.interpreter.tokens.grouping.ParenthesizedToken;

public class EndParenToken extends ClosingToken {

	public EndParenToken(LineInfo line) {
		super(line);
	}

	@Override
	public GroupingException getClosingException(GrouperToken t) {
		return t instanceof ParenthesizedToken ? null
				: new EnumeratedGroupingException(lineInfo,
						grouping_exception_types.MISMATCHED_PARENS);
	}
}
