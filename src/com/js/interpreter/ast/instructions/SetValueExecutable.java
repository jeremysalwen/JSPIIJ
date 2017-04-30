package com.js.interpreter.ast.instructions;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.exceptions.ParsingException;

public interface SetValueExecutable extends Executable {
    public void setAssignedValue(RValue value);

    @Override
    public SetValueExecutable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException;
}
