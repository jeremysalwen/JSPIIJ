package com.js.interpreter.ast.instructions.returnsvalue;

import javax.naming.OperationNotSupportedException;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.exceptions.ConstantCalculationException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.JavaClassBasedType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.PascalArithmeticException;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.tokens.OperatorTypes;

public class BinaryOperatorEvaluation extends DebuggableReturnsValue {
	OperatorTypes operator_type;

	ReturnsValue operon1;

	ReturnsValue operon2;
	LineInfo line;

	public BinaryOperatorEvaluation(ReturnsValue operon1, ReturnsValue operon2,
			OperatorTypes operator, LineInfo line) {
		this.operator_type = operator;
		this.operon1 = operon1;
		this.operon2 = operon2;
		this.line = line;
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	@Override
	public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
			throws RuntimePascalException {
		Object value1 = operon1.getValue(f, main);
		Object value2 = operon2.getValue(f, main);
		return operate(value1, value2);
	}

	public Object operate(Object value1, Object value2)
			throws PascalArithmeticException {
		try {
			/*
			 * TODO: seperate the different type operations into different
			 * classes so there is no runtime type checking.
			 */
			DeclaredType type1 = JavaClassBasedType.anew(value1.getClass());

			DeclaredType type2 = JavaClassBasedType.anew(value2.getClass());
			if (value1 instanceof StringBuilder
					|| value2 instanceof StringBuilder) {
				String val1 = value1.toString();
				String val2 = value2.toString();
				return operator_type.operate(val1, val2);
			}
			DeclaredType gcf = OperatorTypes.get_GCF(type1, type2);
			if (gcf == JavaClassBasedType.Double) {
				double d1 = ((Number) value1).doubleValue();
				double d2 = ((Number) value2).doubleValue();
				return operator_type.operate(d1, d2);
			} else if (gcf == JavaClassBasedType.Long) {
				long l1 = ((Number) value1).longValue();
				long l2 = ((Number) value2).longValue();
				Object result = operator_type.operate(l1, l2);
				return result;
			} else if (gcf == JavaClassBasedType.Integer) {
				long l1 = ((Number) value1).longValue();
				long l2 = ((Number) value2).longValue();
				Object result = operator_type.operate(l1, l2);
				if (result instanceof Number) {
					result = ((Number) result).intValue();
				}
				return result;
			} else if (value1 instanceof Boolean && value2 instanceof Boolean) {
				boolean b1 = (Boolean) value1;
				boolean b2 = (Boolean) value2;
				return operator_type.operate(b1, b2);
			} else if (value1 instanceof Character
					&& value2 instanceof Character) {
				char c1 = (Character) value1;
				char c2 = (Character) value2;
				return operator_type.operate(c1, c2);
			} else {

				return null;
			}
		} catch (OperationNotSupportedException e) {
			throw new RuntimeException(e);
		} catch (ArithmeticException e) {
			throw new PascalArithmeticException(this.line, e);
		}
	}

	@Override
	public String toString() {
		return "(" + operon1 + ") " + operator_type + " (" + operon2 + ')';
	}

	@Override
	public RuntimeType get_type(FunctionDeclaration f) throws ParsingException {
		DeclaredType type1 = operon1.get_type(f).declType;
		DeclaredType type2 = operon2.get_type(f).declType;
		switch (operator_type) {
		case AND:
		case EQUALS:
		case GREATEREQ:
		case GREATERTHAN:
		case LESSEQ:
		case LESSTHAN:
		case NOT:
		case NOTEQUAL:
		case OR:
			return new RuntimeType(JavaClassBasedType.Boolean, false);
		case DIV:
		case MOD:
		case SHIFTLEFT:
		case SHIFTRIGHT:
		case DIVIDE:
		case MULTIPLY:
		case PLUS:
		case XOR:
			return new RuntimeType(OperatorTypes.get_GCF(type1, type2), false);
		case MINUS:
			DeclaredType gcf = OperatorTypes.get_GCF(type1, type2);
			return new RuntimeType(gcf, false);
		default:
			return null;
		}
	}

	@Override
	public Object compileTimeValue() throws ParsingException {
		Object value1 = operon1.compileTimeValue();
		Object value2 = operon2.compileTimeValue();
		if (value1 != null && value2 != null) {
			try {
				return operate(value1, value2);
			} catch (PascalArithmeticException e) {
				throw new ConstantCalculationException(e);
			}
		} else {
			return null;
		}
	}
}
