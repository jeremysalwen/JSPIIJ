package com.js.interpreter.tokens.closing;

import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException.grouping_exception_types;
import com.js.interpreter.exceptions.grouping.GroupingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.grouping.*;

public class EndToken extends ClosingToken {

    public EndToken(LineInfo line) {
        super(line);
    }

    @Override
    public GroupingException getClosingException(GrouperToken t) {
        if (t instanceof BeginEndToken || t instanceof CaseToken
                || t instanceof RecordToken || t instanceof ClassToken) {
            return null;
        } else {
            return new EnumeratedGroupingException(lineInfo,
                    grouping_exception_types.EXTRA_END);
        }
    }
}
