package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.Token;

public class BreakToken extends Token {
    public BreakToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "break";
    }
}
