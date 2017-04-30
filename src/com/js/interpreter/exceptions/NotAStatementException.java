package com.js.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.RValue;

public class NotAStatementException extends ParsingException {

    public NotAStatementException(RValue r) {
        super(r.getLineNumber(), r + " is not an instruction by itself.");
    }

}
