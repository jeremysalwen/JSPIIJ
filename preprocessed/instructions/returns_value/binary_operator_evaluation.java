package preprocessed.instructions.returns_value;

import preprocessed.interpreting_objects.function_on_stack;
import tokens.operator_types;

public class binary_operator_evaluation extends returns_value {
	operator_types operator_type;
	returns_value operon1;
	returns_value operon2;

	public binary_operator_evaluation(returns_value operon1,
			returns_value operon2, operator_types operator) {
		this.operator_type = operator;
		this.operon1 = operon1;
		this.operon2 = operon2;
	}

	@Override
	public Object get_value(function_on_stack f) {
		Object value1 = operon1.get_value(f);
		Object value2 = operon2.get_value(f);
		Class GCF = get_GCF(value1, value2);
		if (GCF == String.class) {
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
		} else if (GCF == Integer.class) {
			int val1 = ((Number) value1).intValue();
			int val2 = ((Number) value2).intValue();
			switch (operator_type) {
			case DIV:
				return val1 / val2;
			case DIVIDE:
				return val1 / val2;
			case GREATEREQ:
				return val1 >= val2;
			}
		}
	}

	Class get_GCF(Object o1, Object o2) {
		if (o1 instanceof String || o2 instanceof String) {
			return String.class;
		}
		if (o1 instanceof Number) {
			if (!(o2 instanceof Number)) {
				return null;
			}
			/* keep order here */
			if (o2 instanceof Double || o1 instanceof Double) {
				return Double.class;
			}
			if (o2 instanceof Float || o1 instanceof Float) {
				return Float.class;
			}
			if (o2 instanceof Long || o1 instanceof Long) {
				return Long.class;
			}
			if (o2 instanceof Integer || o1 instanceof Integer) {
				return Integer.class;
			}
			return null;
		}
		if (o1 instanceof Character && o2 instanceof Character) {
			return Character.class;
		}
		return null;
	}
}
