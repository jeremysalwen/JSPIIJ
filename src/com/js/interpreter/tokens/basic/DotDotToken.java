package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class DotDotToken extends BasicToken {

    public DotDotToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "..";
    }
}
