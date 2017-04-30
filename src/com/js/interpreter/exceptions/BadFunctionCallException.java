package com.js.interpreter.exceptions;

import com.js.interpreter.linenumber.LineInfo;

public class BadFunctionCallException extends ParsingException {
    String functionName;
    boolean functionExists;
    boolean numargsMatch;

    public BadFunctionCallException(LineInfo line, String functionName,
                                    boolean functionExists, boolean numargsMatch) {
        super(line);
        this.functionName = functionName;
        this.functionExists = functionExists;
        this.numargsMatch = numargsMatch;
    }

    @Override
    public String getMessage() {
        if (functionExists) {
            if (numargsMatch) {
                return ("One or more arguments has an incorrect operator when calling function \""
                        + functionName + "\".");
            } else {
                return ("Either too few or two many arguments are being passed to function \""
                        + functionName + "\".");
            }
        } else {
            return ("Attempted to call function \"" + functionName + "\", which is not defined.");
        }
    }

}
