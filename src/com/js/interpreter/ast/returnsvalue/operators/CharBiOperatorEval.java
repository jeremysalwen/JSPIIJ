package com.js.interpreter.ast.returnsvalue.operators;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.BasicType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.exception.PascalArithmeticException;
import com.js.interpreter.runtime.exception.internal.InternalInterpreterException;
import com.js.interpreter.tokens.OperatorTypes;

public class CharBiOperatorEval extends BinaryOperatorEvaluation {

    public CharBiOperatorEval(ReturnsValue operon1, ReturnsValue operon2,
                              OperatorTypes operator, LineInfo line) {
        super(operon1, operon2, operator, line);
    }

    @Override
    public RuntimeType get_type(ExpressionContext f) throws ParsingException {
        switch (operator_type) {
            case EQUALS:
            case GREATEREQ:
            case GREATERTHAN:
            case LESSEQ:
            case LESSTHAN:
            case NOTEQUAL:
                return new RuntimeType(BasicType.Boolean, false);
            default:
                return new RuntimeType(BasicType.Character, false);
        }
    }

    @Override
    public Object operate(Object value1, Object value2)
            throws PascalArithmeticException, InternalInterpreterException {
        char v1 = (char) value1;
        char v2 = (char) value2;
        switch (operator_type) {
            case AND:
                return v1 & v2;
            case DIV:
                return v1 / v2;
            case EQUALS:
                return v1 == v2;
            case GREATEREQ:
                return v1 >= v2;
            case GREATERTHAN:
                return v1 > v2;
            case LESSEQ:
                return v1 <= v2;
            case LESSTHAN:
                return v1 < v2;
            case MINUS:
                return v1 - v2;
            case MOD:
                return v1 % v2;
            case MULTIPLY:
                return v1 * v2;
            case NOTEQUAL:
                return v1 != v2;
            case OR:
                return v1 | v2;
            case PLUS:
                return (char) (v1 + v2);
            case SHIFTLEFT:
                return v1 << v2;
            case SHIFTRIGHT:
                return v1 >> v2;
            case XOR:
                return v1 ^ v2;
            default:
                throw new InternalInterpreterException(line);
        }
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, line);
        } else {
            return new CharBiOperatorEval(
                    operon1.compileTimeExpressionFold(context),
                    operon2.compileTimeExpressionFold(context), operator_type,
                    line);
        }
    }
}
