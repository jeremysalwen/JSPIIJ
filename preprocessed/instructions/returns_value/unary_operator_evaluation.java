package preprocessed.instructions.returns_value;

import preprocessed.interpreting_objects.function_on_stack;
import tokens.operator_token;

public class unary_operator_evaluation extends returns_value {
	public operator_token.types type;
	public returns_value operon;

	public unary_operator_evaluation(returns_value operon,
			operator_token.types operator) {
		this.type = operator;
		this.operon = operon;
	}

	@Override
	public Object get_value(function_on_stack f) {
		Object value = operon.get_value(f);
		switch (type) {
		case PLUS:
			return value;
		case MINUS:
			return -value;
		default:
			return value;
		}
	}

}
