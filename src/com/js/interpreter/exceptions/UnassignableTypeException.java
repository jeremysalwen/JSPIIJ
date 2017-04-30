package com.js.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.RValue;

public class UnassignableTypeException extends ParsingException {

    public UnassignableTypeException(RValue value) {
        super(value.getLineNumber(), "The expression " + value
                + " cannot have a value assigned to it.");
    }

}
