package com.js.interpreter.tokens.value;

import com.js.interpreter.linenumber.LineInfo;

public class CharacterToken extends ValueToken {
    char c;

    public CharacterToken(LineInfo line, char character) {
        super(line);
        this.c = character;
    }

    @Override
    public Object getValue() {
        return c;
    }

}
