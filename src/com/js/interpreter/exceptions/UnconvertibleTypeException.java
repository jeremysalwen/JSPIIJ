package com.js.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.pascaltypes.DeclaredType;

public class UnconvertibleTypeException extends ParsingException {

    public UnconvertibleTypeException(RValue obj,
                                      DeclaredType out, DeclaredType in, boolean implicit) {
        super(obj.getLineNumber(), "The expression " + obj + " is of operator " + in
                + ", which cannot be " + (implicit ? "implicitly " : "")
                + "converted to to the operator " + out + ".");

    }
}
