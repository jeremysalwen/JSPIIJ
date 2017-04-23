package com.js.interpreter.exceptions.grouping;

import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;

public class GroupingException extends ParsingException {

    public GroupingException(LineInfo line, String message) {
        super(line, message);
    }

    public GroupingException(LineInfo line) {
        super(line);
    }
}
