package com.js.interpreter.exceptions;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.DeclaredType;

public class NonArrayIndexed extends ParsingException {
    DeclaredType t;

    public NonArrayIndexed(LineInfo line, DeclaredType t) {
        super(line);
        this.t = t;
    }

    @Override
    public String getMessage() {
        return "Tried to do indexed acess on something which wasn't an array or a string.  It was a "
                + t.toString();
    }
}
