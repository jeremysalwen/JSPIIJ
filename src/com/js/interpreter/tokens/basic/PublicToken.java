package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class PublicToken extends BasicToken {
    public PublicToken(LineInfo lineinfo) {
        super(lineinfo);
    }

    @Override
    public String toString() {
        return "public";
    }
}
