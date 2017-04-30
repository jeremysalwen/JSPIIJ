package com.js.interpreter.ast.returnsvalue;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface RValue {
    Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException;

    RuntimeType get_type(ExpressionContext f)
            throws ParsingException;

    LineInfo getLineNumber();

    /*
     * returns null if not a compile-time constant.
     */
    Object compileTimeValue(CompileTimeContext context)
            throws ParsingException;

    RValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException;

    LValue asLValue(ExpressionContext f);
}
