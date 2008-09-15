package preprocessed.instructions.returns_value;

import preprocessed.function_declaration;
import preprocessed.interpreting_objects.function_on_stack;
import processing.pascal_program;
import tokens.value.operator_types;

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
		Object value1 = operon1.get_value(f);
		Object value2 = operon2.get_value(f);
		if (value1 instanceof String || value2 instanceof String) {
			String val1 = value1.toString();
			String val2 = value2.toString();
			switch (operator_type) {
			case EQUALS:
				return val1.equals(val2);
			case PLUS:
				return new StringBuilder(val1).append(val2).toString();
			default:
				return null;
			}
		} else if (value1 instanceof Double || value2 instanceof Double
				|| value1 instanceof Float || value2 instanceof Float) {
			double d1 = ((Number) value1).doubleValue();
			double d2 = ((Number) value2).doubleValue();
			switch (operator_type) {
			case DIV:
				return ((int) d1) / ((int) d2);
			case DIVIDE:
				return d1 / d2;
			case EQUALS:
				return d1 == d2;
			case GREATEREQ:
				return d1 >= d2;
			case GREATERTHAN:
				return d1 > d2;
			case LESSEQ:
				return d1 <= d2;
			case LESSTHAN:
				return d1 < d2;
			case MINUS:
				return d1 - d2;
			case MOD:
				return d1 % d2;
			case MULTIPLY:
				return d1 * d2;
			case NOTEQUAL:
				return d1 != d2;
			case PLUS:
				return d1 + d2;
			default:
				return null;
			}
		} else if (value1 instanceof Number && value2 instanceof Number) {
			long l1 = ((Number) value1).longValue();
			long l2 = ((Number) value2).longValue();
			switch (operator_type) {
			case DIV:
			case DIVIDE:
				return l1 / l2;
			case EQUALS:
				return (l1 == l2);
			case GREATEREQ:
				return l1 >= l2;
			case GREATERTHAN:
				return l1 > l2;
			case LESSEQ:
				return l1 <= l2;
			case LESSTHAN:
				return l1 < l2;
			case MINUS:
				return l1 - l2;
			case MOD:
				return l1 % l2;
			case MULTIPLY:
				return l1 * l2;
			case NOTEQUAL:
				return l1 != l2;
			case PLUS:
				return l1 + l2;
			case SHIFTLEFT:
				return l1 << l2;
			case SHIFTRIGHT:
				return l1 >> l2;
			case XOR:
				return l1 ^ l2;
			default:
				return null;
			}
		} else if (value1 instanceof Boolean && value2 instanceof Boolean) {
			boolean b1 = (Boolean) value1;
			boolean b2 = (Boolean) value2;
			switch (operator_type) {
			case AND:
				return b1 && b2;
			case EQUALS:
				return b1 == b2;
			case NOTEQUAL:
				return b1 != b2;
			case OR:
				return b1 || b2;
			case XOR:
				return b1 ^ b2;
			default:
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "[" + operon1 + "] " + operator_type + " [" + operon2 + ']';
	}

	public Class get_type(pascal_program p, function_declaration f) {
		Class type1 = operon1.get_type(p, f);
		Class type2 = operon2.get_type(p, f);
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
			return Boolean.class;
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

	public static Class get_GCF(Class one, Class two) {
		if (one == String.class || two == String.class) {
			return String.class;
		}
		if (one == Double.class || two == Double.class) {
			return Double.class;
		}
		if (one == Float.class || two == Float.class) {
			return Float.class;
		}
		if (one == Long.class || two == Long.class) {
			return Long.class;
		}
		if (one == Integer.class || two == Integer.class) {
			return Integer.class;
		}
		if (one == Boolean.class || two == Boolean.class) {
			return Boolean.class;
		}
		return null;
	}
}
