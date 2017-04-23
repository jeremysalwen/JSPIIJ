package com.js.interpreter.tokens.value;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.Token;

public abstract class ValueToken extends Token {

    public ValueToken(LineInfo line) {
        super(line);
    }

    public abstract Object getValue();

    @Override
    public String toString() {
        return getValue().toString();
    }
}
