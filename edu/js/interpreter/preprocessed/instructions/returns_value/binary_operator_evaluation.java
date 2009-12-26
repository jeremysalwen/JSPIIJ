package edu.js.interpreter.preprocessed.instructions.returns_value;

import javax.naming.OperationNotSupportedException;

import edu.js.interpreter.pascal_types.class_pascal_type;
import edu.js.interpreter.pascal_types.pascal_type;
import edu.js.interpreter.preprocessed.function_declaration;
import edu.js.interpreter.preprocessed.interpreting_objects.function_on_stack;
import edu.js.interpreter.tokens.value.operator_types;

public class binary_operator_evaluation implements returns_value {
	operator_types operator_type;

	returns_value operon1;

	returns_value operon2;

	public binary_operator_evaluation(returns_value operon1,
			returns_value operon2, operator_types operator) {
		this.operator_type = operator;
		this.operon1 = operon1;
		this.operon2 = operon2;
	}

	public Object get_value(function_on_stack f) {
		try {
			Object value1 = operon1.get_value(f);
			Object value2 = operon2.get_value(f);
			if (value1 instanceof String || value2 instanceof String) {
				String val1 = value1.toString();
				String val2 = value2.toString();
				operator_type.operate(val1, val2);
			} else if (get_GCF(value1.getClass(), value2.getClass()) == Double.class) {
				double d1 = ((Number) value1).doubleValue();
				double d2 = ((Number) value2).doubleValue();
				return operator_type.operate(d1, d2);
			} else if (get_GCF(value1.getClass(), value2.getClass()) == Long.class) {
				long l1 = ((Number) value1).longValue();
				long l2 = ((Number) value2).longValue();
				return operator_type.operate(l1, l2);

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
		return null;
	}

	@Override
	public String toString() {
		return "[" + operon1 + "] " + operator_type + " [" + operon2 + ']';
	}

	public pascal_type get_type(function_declaration f) {
		pascal_type type1 = operon1.get_type(f);
		pascal_type type2 = operon2.get_type(f);
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
			return class_pascal_type.Boolean;
		case DIV:
		case MOD:
		case SHIFTLEFT:
		case SHIFTRIGHT:
		case DIVIDE:
		case MINUS:
		case MULTIPLY:
		case PLUS:
		case XOR:
			return class_pascal_type.anew(get_GCF(type1.toclass(), type2
					.toclass()));
		default:
			return null;
		}
	}

	public static Class get_GCF(Class one, Class two) {
		if (one == String.class || two == String.class) {
			return String.class;
		}
		if (one == Double.class || two == Double.class) {
			return Double.class;
		}
		if (one == Long.class || two == Long.class ||one == Integer.class || two == Integer.class) {
			return Long.class;
		}
		if (one == Boolean.class || two == Boolean.class) {
			return Boolean.class;
		}
		return null;
	}
}
