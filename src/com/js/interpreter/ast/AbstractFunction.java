package com.js.interpreter.ast;

import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.ArgumentType;
import com.js.interpreter.pascaltypes.DeclaredType;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractFunction implements NamedEntity {

    @Override
    public abstract String name();

    public abstract ArgumentType[] argumentTypes();

    public abstract DeclaredType return_type();

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name());
        result.append('(');
        for (ArgumentType c : argumentTypes()) {
            result.append(c);
            result.append(',');
        }
        result.append(')');
        return result.toString();
    }

    /**
     * @param values
     * @return converted arguments, or null, if they do not fit.
     * @throws ParsingException
     */
    public RValue[] format_args(List<RValue> values,
                                ExpressionContext f) throws ParsingException {
        ArgumentType[] accepted_types = argumentTypes();
        RValue[] result = new RValue[accepted_types.length];
        Iterator<RValue> iterator = values.iterator();
        for (int i = 0; i < accepted_types.length; i++) {
            result[i] = accepted_types[i].convertArgType(iterator, f);
            if (result[i] == null) {/*
                                     * This indicates that it cannot fit.
									 */
                return null;
            }
        }
        if (iterator.hasNext()) {
            return null;
        }
        return result;
    }

    public RValue[] perfectMatch(List<RValue> args,
                                 ExpressionContext context) throws ParsingException {
        ArgumentType[] accepted_types = argumentTypes();
        Iterator<RValue> iterator = args.iterator();
        RValue[] result = new RValue[accepted_types.length];
        for (int i = 0; i < accepted_types.length; i++) {
            result[i] = accepted_types[i].perfectFit(iterator, context);
            if (result[i] == null) {
                return null;
            }
        }
        return result;
    }

    public abstract RValue generatePerfectFitCall(LineInfo line,
                                                  List<RValue> values, ExpressionContext f)
            throws ParsingException;

    public abstract RValue generateCall(LineInfo line,
                                        List<RValue> values, ExpressionContext f)
            throws ParsingException;

}
