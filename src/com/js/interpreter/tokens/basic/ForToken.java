package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class ForToken extends BasicToken {
    public ForToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "for";
    }
}
