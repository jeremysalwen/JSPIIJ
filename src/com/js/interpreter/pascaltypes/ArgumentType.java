package com.js.interpreter.pascaltypes;

import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.exceptions.ParsingException;

import java.util.Iterator;

public interface ArgumentType {
    public RValue convertArgType(Iterator<RValue> args,
                                 ExpressionContext f) throws ParsingException;

    public RValue perfectFit(Iterator<RValue> types,
                             ExpressionContext e) throws ParsingException;

    @SuppressWarnings("rawtypes")
    public Class getRuntimeClass();

}
