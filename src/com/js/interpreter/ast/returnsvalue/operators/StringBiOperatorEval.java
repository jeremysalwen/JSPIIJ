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

public class StringBiOperatorEval extends BinaryOperatorEvaluation {

	public StringBiOperatorEval(ReturnsValue operon1, ReturnsValue operon2,
			OperatorTypes operator, LineInfo line) {
		super(operon1, operon2, operator, line);
	}

	@Override
	public RuntimeType get_type(ExpressionContext f) throws ParsingException {
		switch (operator_type) {
		case EQUALS:
		case NOTEQUAL:
			return new RuntimeType(BasicType.Boolean, false);
		default:
			return new RuntimeType(BasicType.StringBuilder, false);
		}
	}

	@Override
	public Object operate(Object value1, Object value2)
			throws PascalArithmeticException, InternalInterpreterException {
		String v1 = value1.toString();
		String v2 = value2.toString();
		switch (operator_type) {
			case EQUALS:
				return v1.equals(v2);
			case NOTEQUAL:
				return !v1.equals(v2);
			case LESSTHAN:
				return v1.compareTo(v2) < 0;
			case LESSEQ:
				return v1.compareTo(v2) <= 0;
			case GREATEREQ:
				return v1.compareTo(v2) >= 0;
			case GREATERTHAN:
				return v1.compareTo(v2) > 0;
			case PLUS:
				return new StringBuilder(v1).append(v2);
			default:
				throw new InternalInterpreterException(line);
		}
	}

	@Override
	public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
			throws ParsingException {
		Object val = this.compileTimeValue(context);
		if (val != null) {
			return new ConstantAccess(val, line);
		} else {
			return new StringBiOperatorEval(
					operon1.compileTimeExpressionFold(context),
					operon2.compileTimeExpressionFold(context), operator_type,
					line);
		}
	}
}
