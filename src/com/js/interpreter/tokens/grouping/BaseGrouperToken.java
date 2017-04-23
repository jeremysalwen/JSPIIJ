package com.js.interpreter.tokens.grouping;

import com.js.interpreter.linenumber.LineInfo;

public class BaseGrouperToken extends GrouperToken {

    public BaseGrouperToken(LineInfo line) {
        super(line);
    }

    /**
     *
     */
    private static final long serialVersionUID = -2440884131782261439L;

    @Override
    protected String getClosingText() {
        return "[End of file]";
    }

}
