package edu.js.interpreter.preprocessed.instructions.returnsvalue;

import javax.naming.OperationNotSupportedException;

import edu.js.interpreter.pascaltypes.JavaClassBasedType;
import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.tokens.value.OperatorTypes;

public class BinaryOperatorEvaluation implements ReturnsValue {
	OperatorTypes operator_type;

	ReturnsValue operon1;

	ReturnsValue operon2;

	public BinaryOperatorEvaluation(ReturnsValue operon1,
			ReturnsValue operon2, OperatorTypes operator) {
		this.operator_type = operator;
		this.operon1 = operon1;
		this.operon2 = operon2;
	}

	public Object get_value(FunctionOnStack f) {
		try {
			Object value1 = operon1.get_value(f);
			Object value2 = operon2.get_value(f);
			PascalType type1 = operon1.get_type(f.prototype);
			PascalType type2 = operon2.get_type(f.prototype);
			if (value1 instanceof String || value2 instanceof String) {
				String val1 = value1.toString();
				String val2 = value2.toString();
				return operator_type.operate(val1, val2);
			}
			PascalType gcf = get_GCF(type1, type2);
			if (gcf == JavaClassBasedType.Double) {
				double d1 = ((Number) value1).doubleValue();
				double d2 = ((Number) value2).doubleValue();
				return operator_type.operate(d1, d2);
			} else if (gcf == JavaClassBasedType.Long) {
				long l1 = ((Number) value1).longValue();
				long l2 = ((Number) value2).longValue();
				Object result= operator_type.operate(l1, l2);
				return result;
			} else if (value1 instanceof Boolean && value2 instanceof Boolean) {
				boolean b1 = (Boolean) value1;
				boolean b2 = (Boolean) value2;
				return operator_type.operate(b1, b2);
			} else {
				return null;
			}
		} catch (OperationNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "[" + operon1 + "] " + operator_type + " [" + operon2 + ']';
	}

	public PascalType get_type(FunctionDeclaration f) {
		PascalType type1 = operon1.get_type(f);
		PascalType type2 = operon2.get_type(f);
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
			return JavaClassBasedType.Boolean;
		case DIV:
		case MOD:
		case SHIFTLEFT:
		case SHIFTRIGHT:
		case DIVIDE:
		case MINUS:
		case MULTIPLY:
		case PLUS:
		case XOR:
			return get_GCF(type1, type2);
		default:
			return null;
		}
	}

	public static PascalType get_GCF(PascalType one, PascalType two) {
		if (one == JavaClassBasedType.StringBuilder
				|| two == JavaClassBasedType.StringBuilder) {
			return JavaClassBasedType.StringBuilder;
		}
		if (one == JavaClassBasedType.Double || two == JavaClassBasedType.Double) {
			return JavaClassBasedType.Double;
		}
		if (one == JavaClassBasedType.Long || two == JavaClassBasedType.Long
				|| one == JavaClassBasedType.Integer
				|| two == JavaClassBasedType.Integer) {
			return JavaClassBasedType.Long;
		}
		if (one == JavaClassBasedType.Boolean
				|| two == JavaClassBasedType.Boolean) {
			return JavaClassBasedType.Boolean;
		}
		return null;
	}
}
