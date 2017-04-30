package com.js.interpreter.ast.returnsvalue.operators;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.DebuggableRValue;
import com.js.interpreter.ast.returnsvalue.LValue;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.PointerType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import javax.sound.sampled.Line;

public class AddressEval extends DebuggableRValue {
    LValue target;
    LineInfo line;

    public AddressEval(LValue target, LineInfo line) {
        this.target = target;
        this.line = line;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
        return target.getReference(f, main);
    }

    @Override
    public RuntimeType get_type(ExpressionContext f) throws ParsingException {
        return new RuntimeType(new PointerType(target.get_type(f).declType), false);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context) throws ParsingException {
        return null;
    }

    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        return this;
    }
}
