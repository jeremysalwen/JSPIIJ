package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class ThenToken extends BasicToken {
    public ThenToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "then";
    }
}
