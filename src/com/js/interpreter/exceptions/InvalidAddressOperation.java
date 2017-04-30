package com.js.interpreter.exceptions;


import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.linenumber.LineInfo;

public class InvalidAddressOperation extends ParsingException {

    public InvalidAddressOperation(LineInfo line, RValue v) {
        super(line, "The expression "+ v+ " cannot have its address taken.");
    }

    public InvalidAddressOperation(LineInfo line) {
        super(line);
    }
}
