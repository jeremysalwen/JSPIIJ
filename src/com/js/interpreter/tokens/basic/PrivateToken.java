package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class PrivateToken extends BasicToken {
    public PrivateToken(LineInfo lineinfo) {
        super(lineinfo);
    }

    @Override
    public String toString() {
        return "private";
    }
}
