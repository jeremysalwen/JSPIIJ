package com.js.interpreter.tokens.value;

import com.js.interpreter.linenumber.LineInfo;

public class IntegerToken extends ValueToken {
    public int value;

    public IntegerToken(LineInfo line, int i) {
        super(line);
        value = i;
    }

    @Override
    public String toString() {
        return "integer_value_of[" + value + ']';
    }

    @Override
    public Object getValue() {
        return value;
    }
}
