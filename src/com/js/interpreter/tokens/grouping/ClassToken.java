package com.js.interpreter.tokens.grouping;

import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.tokens.Token;

public class ClassToken extends GrouperToken {
    public ClassToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("class ");
        if (next != null) {
            result.append(next).append(' ');
        }
        for (Token t : this.queue) {
            result.append(t).append(' ');
        }
        result.append("end");
        return result.toString();
    }

    @Override
    protected String getClosingText() {
        return "end";
    }
}
