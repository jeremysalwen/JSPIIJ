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
import com.js.interpreter.runtime.variables.ContainsVariables;

public class CloneableObjectCloner implements RValue {
    RValue r;

    public CloneableObjectCloner(RValue r) {
        this.r = r;
    }

    @Override
    public RuntimeType get_type(ExpressionContext f)
            throws ParsingException {
        return r.get_type(f);
    }

    @Override
    public Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        ContainsVariables c = (ContainsVariables) r.getValue(f, main);
        return c.clone();
    }

    @Override
    public LineInfo getLineNumber() {
        return r.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        ContainsVariables c = (ContainsVariables) r
                .compileTimeValue(context);
        return c.clone();
    }

    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new CloneableObjectCloner(r.compileTimeExpressionFold(context));
    }

    @Override
    public LValue asLValue(ExpressionContext f) {
        return null;
    }
}