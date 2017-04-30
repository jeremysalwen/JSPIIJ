package com.js.interpreter.ast.returnsvalue.cloning;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.LValue;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringBuilderCloner implements RValue {
    RValue r;

    @Override
    public RuntimeType get_type(ExpressionContext f)
            throws ParsingException {
        return r.get_type(f);
    }

    @Override
    public Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        StringBuilder other = (StringBuilder) r.getValue(f, main);
        return new StringBuilder(other);
    }

    @Override
    public LineInfo getLineNumber() {
        return r.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object val = r.compileTimeValue(context);
        if (val != null) {
            return new java.lang.StringBuilder((StringBuilder) val);
        }
        return null;
    }

    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new StringBuilderCloner(r);
    }

    @Override
    public LValue asLValue(ExpressionContext f) {
        return null;
    }

    public StringBuilderCloner(RValue r) {
        this.r = r;
    }
}