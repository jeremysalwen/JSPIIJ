package com.js.interpreter.ast.returnsvalue.boxing;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.DebuggableRValue;
import com.js.interpreter.ast.returnsvalue.LValue;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.PointerType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.PascalReference;
import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import javax.sound.sampled.Line;

public class GetAddress extends DebuggableRValue {
    final LValue target;

    public GetAddress(LValue target) throws UnassignableTypeException {
        this.target = target;
    }

    @Override
    public RuntimeType get_type(ExpressionContext f) throws ParsingException {
        return new RuntimeType(new PointerType(target.get_type(f).declType),
                false);
    }

    @Override
    public LineInfo getLineNumber() {
        return target.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        return null;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
       return target.getReference(f, main);
    }

    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
       return this;
    }
}
