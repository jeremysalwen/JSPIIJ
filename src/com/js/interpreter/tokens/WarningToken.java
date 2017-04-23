package com.js.interpreter.tokens;

import com.js.interpreter.linenumber.LineInfo;

public class WarningToken extends Token {
    public String message;

    public WarningToken(LineInfo line, String message) {
        super(line);
        this.message = message;
    }

}
