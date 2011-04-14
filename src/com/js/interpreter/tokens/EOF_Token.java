package com.js.interpreter.tokens;

import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException.grouping_exception_types;
import com.js.interpreter.exceptions.grouping.GroupingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.closing.ClosingToken;
import com.js.interpreter.tokens.grouping.BeginEndToken;
import com.js.interpreter.tokens.grouping.BracketedToken;
import com.js.interpreter.tokens.grouping.GrouperToken;
import com.js.interpreter.tokens.grouping.ParenthesizedToken;

public class EOF_Token extends ClosingToken {
	public EOF_Token(LineInfo line) {
		super(line);
	}

	@Override
	public String toString() {
		return "EOF";
	}

	@Override
	public GroupingException getClosingException(GrouperToken t) {
		if (t instanceof ParenthesizedToken) {
			return new EnumeratedGroupingException(t.lineInfo,
					grouping_exception_types.UNFINISHED_PARENS);
		} else if (t instanceof BeginEndToken) {
			return new EnumeratedGroupingException(t.lineInfo,
					grouping_exception_types.UNFINISHED_BEGIN_END);
		} else if (t instanceof BracketedToken) {
			return new EnumeratedGroupingException(t.lineInfo,
					grouping_exception_types.UNFINISHED_BRACKETS);
		} else {
			return new EnumeratedGroupingException(t.lineInfo,
					grouping_exception_types.UNFINISHED_CONSTRUCT);
		}
	}

}
