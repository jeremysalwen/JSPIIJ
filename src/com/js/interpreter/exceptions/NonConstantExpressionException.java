package com.js.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.RValue;

public class NonConstantExpressionException extends ParsingException {
    public NonConstantExpressionException(RValue value) {
        super(value.getLineNumber(), "The expression \"" + value
                + "\" is not constant.");
    }
}
