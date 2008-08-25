package preprocessed.instructions.returns_value;

import preprocessed.function_declaration;
import preprocessed.interpreting_objects.function_on_stack;
import processing.pascal_program;
import tokens.operator_types;
import exceptions.wrong_type_for_operator_exception;

public class unary_operator_evaluation implements returns_value {
	public operator_types type;
	public returns_value operon;

	public unary_operator_evaluation(returns_value operon,
			operator_types operator) {
		this.type = operator;
		this.operon = operon;
	}

	public Object get_value(function_on_stack f) {
		Object value = operon.get_value(f);
		Class operon_type = value.getClass();
		switch (type) {
		case PLUS:
			return value;
		case MINUS:
			if (operon_type == Integer.class) {
				return new Integer(-(Integer) value);
			} else if (operon_type == Double.class) {
				return new Double(-(Double) value);
			} else {
				throw new wrong_type_for_operator_exception(
						operator_types.MINUS);
			}
		case NOT:
			assert (operon_type == Boolean.class);
			return new Boolean(!((Boolean) value));
		default:
			System.err.println("Operator type " + type
					+ " is not a unary operator");
			return value;
		}
	}

	@Override
	public String toString() {
		return "operator [" + type + "] on [" + operon + ']';
	}

	public Class get_type(pascal_program p,function_declaration f) {
		return operon.get_type(p,f);
	}

}
