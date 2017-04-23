package com.js.interpreter.exceptions;

import com.js.interpreter.linenumber.LineInfo;

public class NoSuchFunctionOrVariableException extends ParsingException {

    public NoSuchFunctionOrVariableException(LineInfo line, String token) {
        super(line, token + " is not a variable or function name");
    }

}
