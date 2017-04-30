package com.js.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.RValue;

public class NonIntegerIndexException extends ParsingException {
    RValue value;

    public NonIntegerIndexException(RValue value) {
        super(value.getLineNumber());
        this.value = value;
    }

    @Override
    public String getMessage() {
        return "Array indexes must be integers: " + value;
    }
}
