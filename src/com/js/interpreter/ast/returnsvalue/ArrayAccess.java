package com.js.interpreter.ast.returnsvalue;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.ArrayReference;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.exceptions.UnassignableTypeException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.ArrayType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.PascalIndexOutOfBoundsException;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.lang.reflect.Array;

public class ArrayAccess extends DebuggableLValue {
    RValue container;
    RValue index;
    int offset;

    public ArrayAccess(RValue container, RValue index, int offset) {
        this.container = container;
        this.index = index;
        this.offset = offset;
    }

    @Override
    public RuntimeType get_type(ExpressionContext f) throws ParsingException {
        RuntimeType r = (container.get_type(f));
        return new RuntimeType(((ArrayType<?>) r.declType).element_type,
                r.writable);
    }

    @Override
    public LineInfo getLineNumber() {
        return index.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object cont = container.compileTimeValue(context);
        Object ind = index.compileTimeValue(context);
        if (ind == null || cont == null) {
            return null;
        } else {
            return Array.get(cont, ((Integer) ind) - offset);
        }
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Object cont = container.getValue(f, main);
        Integer ind = (Integer) index.getValue(f, main);
        try {
            return Array.get(cont, ind - offset);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PascalIndexOutOfBoundsException(this.getLineNumber(),
                    ind, offset, offset + ((Object[]) cont).length - 1);
        }
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
        Object cont = container.getValue(f, main);
        Integer ind = (Integer) index.getValue(f, main);
        return new ArrayReference(cont, ind, offset);
    }

    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new ArrayAccess(container.compileTimeExpressionFold(context),
                index.compileTimeExpressionFold(context), offset);
    }

}
