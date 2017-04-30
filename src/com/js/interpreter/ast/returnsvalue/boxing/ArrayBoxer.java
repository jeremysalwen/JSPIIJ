package com.js.interpreter.ast.returnsvalue.boxing;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.DebuggableRValue;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.ArgumentType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.lang.reflect.Array;

public class ArrayBoxer extends DebuggableRValue {
    RValue[] values;
    ArgumentType type;
    LineInfo line;

    public ArrayBoxer(RValue[] value, ArgumentType elementType,
                      LineInfo line) {
        this.values = value;
        this.type = elementType;
        this.line = line;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public RuntimeType get_type(ExpressionContext f) throws ParsingException {
        throw new ParsingException(
                line,
                "Attempted to get operator of varargs boxer. This should not happen as we are only supposed to pass varargs to plugins");
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Object[] result = (Object[]) Array.newInstance(type.getRuntimeClass(),
                values.length);
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].getValue(f, main);
        }
        return result;
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object[] result = (Object[]) Array.newInstance(type.getRuntimeClass(),
                values.length);
        for (int i = 0; i < values.length; i++) {
            Object val = values[i].compileTimeValue(context);
            if (val == null) {
                return null;
            } else {
                result[i] = val;
            }
        }
        return result;
    }


    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        RValue[] val = new RValue[values.length];
        for (int i = 0; i < values.length; i++) {
            val[i] = values[i].compileTimeExpressionFold(context);
        }
        return new ArrayBoxer(val, type, line);
    }
}
