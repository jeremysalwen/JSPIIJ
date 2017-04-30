package com.js.interpreter.ast.returnsvalue.boxing;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.DebuggableRValue;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.BasicType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringBuilderBoxer extends DebuggableRValue {
    RValue value;

    public StringBuilderBoxer(RValue value) {
        this.value = value;
    }

    @Override
    public RuntimeType get_type(ExpressionContext f) throws ParsingException {
        return new RuntimeType(BasicType.create(String.class), false);
    }

    @Override
    public LineInfo getLineNumber() {
        return value.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object other = value.compileTimeValue(context);
        if (other != null) {
            return ((StringBuilder) other).toString();
        }
        return null;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Object other = value.getValue(f, main);
        return ((StringBuilder) other).toString();
    }

    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, value.getLineNumber());
        } else {
            return new StringBuilderBoxer(
                    value.compileTimeExpressionFold(context));
        }
    }
}
