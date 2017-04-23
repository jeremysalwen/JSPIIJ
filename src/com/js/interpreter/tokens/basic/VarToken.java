package com.js.interpreter.tokens.basic;

import com.js.interpreter.linenumber.LineInfo;

public class VarToken extends BasicToken {
    public VarToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "var";
    }
}
